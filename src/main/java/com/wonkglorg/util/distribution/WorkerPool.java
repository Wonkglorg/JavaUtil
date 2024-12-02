package com.wonkglorg.util.distribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * A Worker pool represents a set of workers all doing the same type of tasks on a given set of jobs
 * provided by the {@link WorkDistributor}
 */
public class WorkerPool<T> {
	/** Used for naming pools */
	private static AtomicInteger poolIndex = new AtomicInteger(1);
	/** The name of this pool */
	private final String poolName;
	private final BlockingQueue<WeightedJob<T>> taskQueue;
	private final BiConsumer<Worker<T>, T> workerJob;
	private final List<Worker<T>> workers;
	private final int workerCount;
	private final Predicate<T> validateWorkerForJob;
	private final int priority;
	private final int capacity;

	/**
	 * @param poolName the name of this pool
	 * @param workerCount how many workers are in this pool
	 * @param priority the priority of this pool compared to others (higher value = higher priority)
	 * @param capacity how many tasks they can have in their queue
	 * @param workerJob the job they should execute
	 * @param validateWorkerForJob weather or not this pool qualifies for a job
	 */
	public WorkerPool(String poolName, int workerCount, int priority, int capacity,
			BiConsumer<Worker<T>, T> workerJob, Predicate<T> validateWorkerForJob) {
		this.poolName = poolName;
		this.workerCount = workerCount;
		this.priority = priority;
		this.capacity = capacity;
		this.workerJob = workerJob;
		this.validateWorkerForJob = validateWorkerForJob;
		this.taskQueue = new LinkedBlockingQueue<>(capacity);
		this.workers = new ArrayList<>();
	}

	/**
	 * @param workerCount how many workers are in this pool
	 * @param priority the priority of this pool compared to others (higher value = higher priority)
	 * @param capacity how many tasks they can have in their queue
	 * @param workerJob the job they should execute
	 * @param validateWorkerForJob weather or not this pool qualifies for a job
	 */
	public WorkerPool(int workerCount, int capacity, int priority,
			BiConsumer<Worker<T>, T> workerJob,
			Predicate<T> validateWorkerForJob) {
		this("WorkerPool%s".formatted(poolIndex.getAndIncrement()), workerCount, priority, capacity,
				workerJob, validateWorkerForJob);
	}

	/**
	 * Starts the workers
	 */
	public void startWorkers() {
		for (int i = 0; i < workerCount; i++) {
			Worker<T> workerThread = new Worker<>(taskQueue, workerJob);
			workerThread.start();
			workers.add(workerThread);
		}
	}

	//todo:jmd how to ensure all files that need to be processed get processed before shutting down?
	public void stopWorkers() {
		for (Thread worker : workers) {
			worker.interrupt();
		}
	}

		/**
	 * Sets the callback to use for workers of this pool
	 * @param workerCallBack the callback (the job that ran, the time it took from start to end in ms)
	 */
	public void setJobCallBackForWorkers(BiConsumer<WeightedJob<T>, Long> workerCallBack) {
		workers.forEach(worker -> worker.setJobFinishCallBack(workerCallBack));
	}

	public boolean isAvailable() {
		return taskQueue.remainingCapacity() > 0;
	}

	/**
	 * Assigns a task to this workerpool
	 *
	 * @param task the task to assign
	 */
	public void assignJob(WeightedJob<T> task) throws InterruptedException {
		taskQueue.put(task);
	}

	/**
	 * Check if this worker pool can handle the task (does not check if available only if they can
	 * work on this task based on their predicate)
	 *
	 * @param task the task to check
	 * @return true if they can work on it false otherwise
	 */
	public boolean canHandle(T task) {
		return validateWorkerForJob.test(task);
	}

	/**
	 * The priority of this worker pool (lower values = higher priority)
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Returns info on how many tasks are in this queue
	 *
	 * @return the amount of tasks
	 */
	public int getTaskCount() {
		return taskQueue.size();
	}

	/**
	 * Gets the total capacity of this workerpool
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Gets the remaining capacity of this workerpool
	 */
	public int getAvailableCapacity() {
		return taskQueue.remainingCapacity();
	}

	/**
	 * Gives a score on how suitable this pool is to take a job (used to compare to other tasks to
	 * find the most likely best case, does not consider of this pool can handle the task (should be
	 * done beforehand))
	 *
	 * @return a score based on suitability (if the score is negative it is not available to take any
	 * new jobs and should not be used
	 */
	public int getSuitability() {
		return getSuitability(null);
	}

	/**
	 * Gives a score on how suitable this pool is to take a job (used to compare to other tasks to
	 * find the most likely best case)
	 *
	 * @return a score based on suitability (if the score is negative it is not available to take any
	 * new jobs and should not be used
	 */
	public int getSuitability(T task) {
		if (!isAvailable() || !canHandle(task)) {
			return -99;
		}

		double capacityFactor = (double) getAvailableCapacity() / getCapacity();
		int availableWorkers = 0;
		for (var worker : workers) {
			availableWorkers += worker.isAvailable() ? 1 : 0;
		}
		double priorityFactor = 1.0 * getPriority();
		double suitabilityScore = capacityFactor + priorityFactor + availableWorkers;

		return (int) (suitabilityScore * 100);
	}


	/**
	 * @return a map of each workers completed jobs and their taken duration
	 */
	public Map<String, Worker.WorkerJobData<T>> getCompletionTimeStats() {
		Map<String, Worker.WorkerJobData<T>> completionTimeStats = new HashMap<>();
		for (var worker : workers) {
			completionTimeStats.put(worker.getWorkerName(), worker.getJobDurations());
		}
		return completionTimeStats;
	}

	public String getPoolName() {
		return poolName;
	}
}
