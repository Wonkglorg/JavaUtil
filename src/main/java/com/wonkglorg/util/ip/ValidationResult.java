package com.wonkglorg.util.ip;

@SuppressWarnings("unused")
public class ValidationResult<T extends IP<?>> {
	private final T ip;
	private final MalformedIpException exception;

	public ValidationResult(T ip, MalformedIpException exception) {
		this.ip = ip;
		this.exception = exception;
	}

	public ValidationResult(T ip, String exceptionMessage) {
		this.ip = ip;
		this.exception = new MalformedIpException(exceptionMessage);
	}

	public ValidationResult(T ip) {
		this.ip = ip;
		this.exception = null;
	}

	public ValidationResult(String exceptionMessage) {
		this.ip = null;
		this.exception = new MalformedIpException(exceptionMessage);
	}

	public ValidationResult(MalformedIpException exception) {
		this.ip = null;
		this.exception = exception;
	}

	public T getIp() {
		return ip;
	}

	/**
	 * If the ip is not null and has not thrown an exception while creation
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean isValid() {
		return ip != null && exception == null;
	}

	/**
	 * Gets the exception associated with this result if one occured
	 */
	public MalformedIpException getException() {
		return exception;
	}

	/**
	 * If the result has an exception
	 */
	public boolean hasException() {
		return exception != null;
	}

	/**
	 * If the result has a valid ip (if an error occured no ip is contained)
	 */
	public boolean hasIp() {
		return ip != null;
	}
}
