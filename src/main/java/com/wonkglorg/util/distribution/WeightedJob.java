package com.wonkglorg.util.distribution;

import java.util.Objects;

/**
 * WeightedJob represents a job with a name/priority/weight.
 */
public class WeightedJob<T> implements Comparable<WeightedJob<T>> {
	/** Name of the task */
	private final String taskName;
	/** The job to run */
	private final T job;
	/** The weight of this job,(higher values = higher priority) */
	private final int weight;
	/** When this job was created */
	private final long creationTime;

	public WeightedJob(String taskName, T task, int weight) {
		this.taskName = taskName;
		this.job = task;
		this.weight = weight;
		this.creationTime = System.currentTimeMillis();
	}

	public String getTaskName() {
		return taskName;
	}

	public T getJob() {
		return job;
	}

	public int getWeight() {
		return weight;
	}

	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public String toString() {
		return "WeightedTask{taskName='%s', job=%s, weight=%s, submitTime=%s}".formatted(taskName, job,
				weight, creationTime);
	}

	@Override
	public int compareTo(WeightedJob<T> other) {
		return Integer.compare(this.weight, other.weight);  // Lower weight = higher priority
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof WeightedJob<?> that)) {
			return false;
		}
		return weight == that.weight && creationTime == that.creationTime && Objects.equals(taskName,
				that.taskName) && Objects.equals(job, that.job);
	}

	@Override
	public int hashCode() {
		return Objects.hash(taskName, job, weight, creationTime);
	}
}
