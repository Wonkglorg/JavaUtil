package com.wonkglorg.util.programms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ProgrammRequest {
	/**
	 * The directory to execute the programm in
	 */
	private String directory;

	/**
	 * List of executable options
	 */
	private final Map<String, String> options = new HashMap<>();
	private Consumer<String> callback = null;
	private Consumer<String> errorCallback = null;

	public ProgrammRequest(String directory, Consumer<String> callback,
			Consumer<String> errorCallback) {
		this.directory = directory;
		this.callback = callback;
		this.errorCallback = errorCallback;
	}

	public ProgrammRequest(String directory, Consumer<String> callBack) {
		this(directory, callBack, null);
	}

	public ProgrammRequest(Consumer<String> callBack) {
		this(null, callBack);
	}

	public ProgrammRequest(String directory) {
		this(directory, null);
	}

	/**
	 * Get the working directory in which the executable will be run
	 *
	 * @return Working directory
	 */

	public String getDirectory() {
		return directory;
	}

	/**
	 * Sets the working directory in which the executable will be run
	 *
	 * @param directory Working directory
	 * @return YtDlpRequest
	 */
	public ProgrammRequest setDirectory(String directory) {
		this.directory = directory;
		return this;
	}

	/**
	 * Gets the options set for this request
	 */
	public Map<String, String> getOptions() {
		return options;
	}

	/**
	 * Add an option to the request
	 *
	 * @param key the option key
	 * @return YtDlpRequest
	 */
	public ProgrammRequest addOption(String key) {
		options.put(key, null);
		return this;
	}

	/**
	 * Add an option to the request
	 *
	 * @param key the option key
	 * @param value the option value
	 * @return YtDlpRequest
	 */
	public ProgrammRequest addOption(String key, String value) {
		options.put(key, value);
		return this;
	}

	/**
	 * Add an option to the request
	 *
	 * @param key the option key
	 * @param value the option value
	 * @return YtDlpRequest
	 */
	public ProgrammRequest addOption(String key, int value) {
		options.put(key, String.valueOf(value));
		return this;
	}

	/**
	 * The callback to run for each line when retrieving programm progress
	 *
	 * @param callBack
	 * @return
	 */
	public ProgrammRequest setCallback(Consumer<String> callBack) {
		this.callback = callBack;
		return this;
	}

	public ProgrammRequest setErrorCallback(Consumer<String> callBack) {
		this.callback = callBack;
		return this;
	}

	public Consumer<String> getErrorCallback() {
		return errorCallback;
	}

	public Consumer<String> getCallback() {
		return callback;
	}


	/**
	 * Transform options to a string that the executable will execute
	 *
	 * @return Command string
	 */
	protected String buildOptions() {
		StringBuilder builder = new StringBuilder();

		// Build options strings
		Iterator<Map.Entry<String, String>> it = options.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> option = it.next();

			String name = option.getKey();
			String value = option.getValue();

			if (value == null) {
				String optionFormatted = String.format("%s", name).trim();
				builder.append(optionFormatted).append(" ");
				it.remove();
				continue;
			}

			String optionFormatted = String.format("%s %s", name, value).trim();
			builder.append(optionFormatted).append(" ");

			it.remove();
		}

		return builder.toString().trim();
	}

	@Override
	protected Object clone() {
		ProgrammRequest clone = new ProgrammRequest(directory, callback, errorCallback);
		clone.options.putAll(this.options);
		return clone;
	}

	@Override
	public String toString() {
		return "ProgrammRequest{" + "directory='" + directory + '\'' + ", options=" + options
				+ ", callback=" + callback + ", errorCallback=" + errorCallback + '}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof ProgrammRequest that)) {
			return false;
		}
		return Objects.equals(directory, that.directory) && Objects.equals(options, that.options)
				&& Objects.equals(callback, that.callback) && Objects.equals(errorCallback,
				that.errorCallback);
	}

	@Override
	public int hashCode() {
		return Objects.hash(directory, options, callback, errorCallback);
	}
}
