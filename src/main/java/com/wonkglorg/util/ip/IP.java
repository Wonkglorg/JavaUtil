package com.wonkglorg.util.ip;

import java.util.Optional;

public interface IP {
	/**
	 * If the optional is empty, no errors were thrown when validating the ip, else the error
	 * contains
	 * the text associated with the cause
	 *
	 * @param ip the ip string to test
	 */
	Optional<MalformedIpException> validateIp(String ip);

	/**
	 * @param ip the ip string to test
	 * @return True if the ip is valid, false otherwise
	 */
	default boolean isValidIp(String ip) {
		return validateIp(ip).isEmpty();
	}
    /*

    IP Validation
    https://stackoverflow.com/questions/21631669/regular-expression-regex-for-ipv6-separate-from
    -ipv4
     */
}
