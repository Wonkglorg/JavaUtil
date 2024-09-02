package com.wonkglorg.util.ip;

/**
 * Thrown by {@link IPv4} and {@link IPv6}
 */
public class MalformedIpException extends RuntimeException {
	public MalformedIpException(String message) {
		super(message);
	}
}
