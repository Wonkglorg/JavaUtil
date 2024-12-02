package com.wonkglorg.util.distribution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Distributes tasks to worker pools assigned to the distributor based on the best suited pick
 *
 * @param <T>
 */
public class WorkDistributor<T> {
	private final List<WorkerPool<T>> workerPools = new ArrayList<>();
	private final PriorityBlockingQueue<WeightedJob<T>> globalTaskQueue =
			new PriorityBlockingQueue<>();
	private boolean isRunning = false;
	private Thread dispatcherThread;
	private int jobCounter = 0;

	/**
	 * Start the dispatcher and workers.
	 */
	public void start() {
		if (isRunning) {
			return;
		}
		isRunning = true;

		workerPools.sort(Comparator.comparingInt(WorkerPool::getPriority));

		dispatcherThread = new Thread(this::dispatchTasks);
		dispatcherThread.start();
		workerPools.forEach(WorkerPool::startWorkers);
	}

	/**
	 * Stop all workers and dispatcher gracefully.
	 */
	public void shutdown() {
		if (!isRunning) {
			return;
		}
		isRunning = false;
		workerPools.forEach(WorkerPool::stopWorkers);
		dispatcherThread.interrupt();
	}

	/**
	 * Dispatch tasks to available workers based on their priority and availability.
	 */
	private void dispatchTasks() {
		try {
			while (isRunning) {
				WeightedJob<T> weightJob = globalTaskQueue.take();
				boolean taskAssigned = false;

				Optional<WorkerPool<T>> bestWorkerPool = getBestWorkerPoolForJob(weightJob.getJob());

				if (bestWorkerPool.isPresent()) {
					bestWorkerPool.get().assignJob(weightJob);
					taskAssigned = true;
				}

				if (!taskAssigned) {
					globalTaskQueue.put(weightJob);
				}
				//wait a bit to not constantly recheck if non are available currently
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Gets the best workerpool suited for the job based on {@link WorkerPool#getSuitability(Object)}
	 *
	 * @param task the task to check
	 * @return a workerpool or an empty optional if non available
	 */
	private Optional<WorkerPool<T>> getBestWorkerPoolForJob(T task) {
		Map<Integer, WorkerPool<T>> workerLikeliness = new ConcurrentHashMap<>();

		for (WorkerPool<T> workerPool : workerPools) {
			int suitability = workerPool.getSuitability(task);
			workerLikeliness.put(suitability, workerPool);
		}

		return workerLikeliness.entrySet().stream().filter(entry -> entry.getKey() > 0)
				.max(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getValue);
	}

	/**
	 * Add a task to the global task queue with a specified weight.
	 *
	 * @param job The task/job to add.
	 * @param weight The priority/weight of the task (lower values indicate higher priority).
	 */
	public void addTask(String taskName, T job, int weight) {
		globalTaskQueue.add(new WeightedJob<>(taskName, job, weight));
	}

	/**
	 * Add a task to the global task queue with a default weight of 2 and a generic "job*num*" name
	 *
	 * @param job The task/job to add.
	 */
	public void addTask(T job) {
		addTask("Job%d".formatted(jobCounter++), job, 2);
	}

	/**
	 * Add a new worker pool to process jobs.
	 *
	 * @param workerCount Number of workers in this pool.
	 * @param workerJob The job each worker should execute.
	 * @param capacity how many jobs this pool can hold to compute in the future
	 * @param validateWorkerForJob Predicate to validate if a worker can handle the task.
	 * @param priority Priority of this worker pool (lower values mean higher priority).
	 */
	public void addWorkerPool(int workerCount, int priority, int capacity, BiConsumer<Worker<T>, T> workerJob,
			Predicate<T> validateWorkerForJob) {
		addWorkerPool(
				new WorkerPool<>(workerCount, priority, capacity, workerJob, validateWorkerForJob));
	}

	/**
	 * Add a new worker pool to process jobs.
	 */
	public void addWorkerPool(WorkerPool<T> workerPool) {
		workerPools.add(workerPool);
	}

	/**
	 * Get all durations for the jobs. A Map consisting of (workerPoolName -> workerName,WorkJobData)
	 */
	public Map<String, Map<String, Worker.WorkerJobData<T>>> getJobDurations() {
		Map<String, Map<String, Worker.WorkerJobData<T>>> workPoolCompletions = new HashMap<>();
		for (var pool : workerPools) {
			workPoolCompletions.put(pool.getPoolName(), pool.getCompletionTimeStats());
		}
		return workPoolCompletions;
	}
}
