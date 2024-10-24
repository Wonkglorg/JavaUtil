package com.wonkglorg.util.distribution;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Worker<T> extends Thread {
	private static final AtomicInteger workerIndex = new AtomicInteger(1);
	private static int MAX_MAP_SIZE = 100;
	/** Keeps track of job runtime durations */
	private final Map<WeightedJob<T>, Long> jobDurations;
	/** Queue of all jobs to be executed (shared per {@link WorkerPool} this worker is part of) */
	private final BlockingQueue<WeightedJob<T>> jobQueue;
	/** The Job a worker executes on the {@link #jobQueue} */
	private final Consumer<T> workerJob;
	/** If this worker is available to process an element */
	private boolean isAvailable = false;
	private final String workerName;

	/**
	 * @param workerName the name of the worker
	 * @param taskQueue the task queue it should retrieve its jobs from
	 * @param workerJob the work to execute on a job
	 */
	public Worker(String workerName, BlockingQueue<WeightedJob<T>> taskQueue,
			Consumer<T> workerJob) {
		this.workerName = workerName;
		this.jobQueue = taskQueue;
		this.workerJob = workerJob;
		jobDurations = new ConcurrentHashMap<>(MAX_MAP_SIZE);
	}
	/**
´´
	 * @param taskQueue the task queue it should retrieve its jobs from
	 * @param workerJob the work to execute on a job
	 */
	public Worker(BlockingQueue<WeightedJob<T>> taskQueue, Consumer<T> workerJob) {
		this("Worker%s".formatted(workerIndex.getAndIncrement()), taskQueue, workerJob);
	}


	/**
	 * If this worker is available to take a new job
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	@Override
	public void run() {
		try {
			while (true) {
				isAvailable = true;
				WeightedJob<T> job = jobQueue.take();
				isAvailable = false;
				long startTime = System.currentTimeMillis();
				workerJob.accept(job.getJob());
				if (MAX_MAP_SIZE != 0) {
					jobDurations.put(job, System.currentTimeMillis() - startTime);
				}
				cleanUpOldEntries();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Cleans up old entries to not cause memory leaks over longer periods of running
	 */
	private void cleanUpOldEntries() {
		if (jobDurations.size() > MAX_MAP_SIZE) {
			WeightedJob<T> oldestJob = Collections.min(jobDurations.keySet(),
					Comparator.comparingLong(WeightedJob::getCreationTime));
			jobDurations.remove(oldestJob);
		}
	}

	/**
	 * How many job duration entries should be stored before deleting the oldest.
	 */
	public static void setMaxJobDurationLogs(int maxMapSize) {
		MAX_MAP_SIZE = Math.max(0, maxMapSize);
	}

	public String getWorkerName() {
		return workerName;
	}

	/**
	 * Gets the duration of the last x jobs based on {@link #MAX_MAP_SIZE}
	 */
	public WorkerJobData<T> getJobDurations() {
		int numberOfJobs = jobDurations.size();
		long minDuration = 0;
		long maxDuration = 0;

		if (numberOfJobs > 0) {
			minDuration = jobDurations.values().stream().min(Long::compareTo).get();
			maxDuration = jobDurations.values().stream().max(Long::compareTo).get();
		}

		return new WorkerJobData<>(minDuration, maxDuration, jobDurations);
	}

	public record WorkerJobData<T>(long minDuration, long maxDuration,
																 Map<WeightedJob<T>, Long> timings) {
	}
}
