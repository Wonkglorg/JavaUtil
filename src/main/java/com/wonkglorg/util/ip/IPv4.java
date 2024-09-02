package com.wonkglorg.util.ip;

import com.wonkglorg.util.string.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;

/**
 * Represents an IPv4
 */
public class IPv4 implements IP {

	private int[] ip = new int[4];

	/**
	 * Creates a new ip from its 4 parts
	 *
	 * @param ip the 4 parts of the ip address
	 * @throws IllegalArgumentException if the ip has not 4 parts or a part is not between 0 and 255
	 */
	public IPv4(int[] ip) throws IllegalArgumentException {
		validateIpParts(ip).ifPresent(e -> {
			throw e;
		});

		this.ip = Arrays.copyOf(ip, 4);
	}

	/**
	 * Creates a new ip from its 4 parts
	 *
	 * @param ip1 first ip part
	 * @param ip2 second ip part
	 * @param ip3 third ip part
	 * @param ip4 fourth ip part
	 * @throws IllegalArgumentException if the ip has not 4 parts or a part is not between 0 and 255
	 */
	public IPv4(int ip1, int ip2, int ip3, int ip4) throws IllegalArgumentException {
		this(new int[]{ip1, ip2, ip3, ip4});
	}

	/**
	 * Creates a new ip from a string formatted as "xxx.xxx.xxx.xxx"
	 *
	 * @param ip
	 * @throws IllegalArgumentException if the ip is not in the correct format
	 */
	public IPv4(String ip) throws IllegalArgumentException {
		int[] parts = convertToIpParts(ip);
		this.ip = Arrays.copyOf(parts, 4);
	}

	/**
	 * Validates and Converts a string formatted as "xxx.xxx.xxx.xxx" to an IPv4
	 *
	 * @param ip the ip to convert
	 * @return the IPv4
	 * @throws IllegalArgumentException if the ip is not in the correct format
	 */
	private int[] convertToIpParts(String ip) {
		validateIp(ip).ifPresent(e -> {
			throw e;
		});
		String[] parts = ip.split("\\.");
		int[] ipArray = new int[4];
		for (int i = 0; i < 4; i++) {
			ipArray[i] = Integer.parseInt(parts[i]);
		}
		return ipArray;
	}

	/**
	 * Validated an ipv4 address formatted as "xxx.xxx.xxx.xxx"
	 *
	 * @param ip the ip string to test
	 * @return an empty optional if valid else the error it threw
	 */
	@Override
	public Optional<MalformedIpException> validateIp(String ip) {
		if (ip == null || ip.isBlank()) {
			return Optional.of(new MalformedIpException("IP cannot be null or blank"));
		}

		if (StringUtils.countOccurrences(ip, '.') > 3) {
			return Optional.of(new MalformedIpException("Malformed Ip"));
		}

		String[] parts = ip.split("\\.");

		if (parts.length != 4) {
			return Optional.of(
					new MalformedIpException("IP must have 4 parts but found " + parts.length));
		}

		int[] ipArray = new int[4];
		try {
			for (int i = 0; i < 4; i++) {
				ipArray[i] = Integer.parseInt(parts[i]);
			}
		} catch (NumberFormatException e) {
			return Optional.of(new MalformedIpException(e.getMessage()));
		}

		return validateIpParts(ipArray);
	}

	/**
	 * Validates an ip based on its 4 int parts
	 *
	 * @return an empty optional if valid else the error it threw
	 */
	public Optional<MalformedIpException> validateIpParts(int[] ipParts) {
		if (ipParts.length != 4) {
			return Optional.of(
					new MalformedIpException("IP must have 4 parts but found " + ipParts.length));
		}

		for (int i = 0; i < 4; i++) {
			if (ipParts[i] < 0 || ipParts[i] > 255) {
				return Optional.of(new MalformedIpException(
						"IP parts must be between 0 and 255 but was " + ipParts[i] + " at position " + i));
			}
		}
		return Optional.empty();
	}


	/**
	 * Returns the ip as an array of integers
	 *
	 * @param subnetMask the subnet mask
	 * @return the network address
	 */
	public IPv4 getNetworkAddress(IPv4 subnetMask) {
		return IPv4.getNetworkAddress(this, subnetMask);
	}

	/**
	 * Returns the network address of the ip
	 *
	 * @param cidr mask from cidr notation
	 * @return the broadcast address
	 */
	public IPv4 getNetworkAddress(int cidr) {
		return getNetworkAddress(this, cidr);
	}


	/**
	 * Returns the broadcast address of the ip
	 *
	 * @param subnetMask the subnet mask
	 * @return the broadcast address
	 */
	public IPv4 getBroadcastAddress(IPv4 subnetMask) {
		return IPv4.getBroadcastAddress(this, subnetMask);
	}

	/**
	 * Returns the broadcast address of the ip
	 *
	 * @param cidr mask from cidr notation
	 * @return the broadcast address
	 */
	public IPv4 getBroadcastAddress(int cidr) {
		IPv4 subnetMask = IPv4.maskFromCidr(cidr);
		return getBroadcastAddress(this, subnetMask);
	}

	/**
	 * Returns the network address
	 *
	 * @param ip the ip
	 * @param subnetMask the subnet mask
	 * @return the network address
	 */
	public static IPv4 getNetworkAddress(IPv4 ip, IPv4 subnetMask) {
		return calculateAddress(ip, subnetMask, (a, b) -> a & b);
	}

	/**
	 * Returns the network address of the ip
	 *
	 * @param cidr mask from cidr notation
	 * @return the broadcast address
	 */
	public static IPv4 getNetworkAddress(IPv4 ip, int cidr) {
		IPv4 subnetMask = IPv4.maskFromCidr(cidr);
		return getNetworkAddress(ip, subnetMask);
	}


	/**
	 * Returns the broadcast address
	 *
	 * @param ip the ip
	 * @param subnetMask the subnet mask
	 * @return the broadcast address
	 */
	public static IPv4 getBroadcastAddress(IPv4 ip, IPv4 subnetMask) {
		return calculateAddress(ip, subnetMask, (a, b) -> a | ~b & 0xFF);
	}

	/**
	 * Returns the broadcast address of the ip
	 *
	 * @param cidr mask from cidr notation
	 * @return the broadcast address
	 */
	public static IPv4 getBroadcastAddress(IPv4 iPv4, int cidr) {
		IPv4 subnetMask = IPv4.maskFromCidr(cidr);
		return getBroadcastAddress(iPv4, subnetMask);
	}

	/**
	 * Executes an operation
	 *
	 * @param ip the initial {@link IPv4}
	 * @param subnetMask the second {@link IPv4}
	 * @param operator the operation to do
	 * @return the new Calculated {@link IPv4}
	 */
	private static IPv4 calculateAddress(IPv4 ip, IPv4 subnetMask, IntBinaryOperator operator) {
		Objects.requireNonNull(subnetMask);
		Objects.requireNonNull(ip);
		Objects.requireNonNull(operator);
		int[] result = new int[4];
		for (int i = 0; i < 4; i++) {
			result[i] = operator.applyAsInt(ip.ip[i], subnetMask.ip[i]);
		}
		return new IPv4(result);
	}

	public static IPv4 maskFromCidr(int cidr) {
		validateCIDR(cidr).ifPresent(e -> {
			throw e;
		});
		int mask = 0xFFFFFFFF << (32 - cidr);
		return new IPv4((mask >> 24) & 0xFF, (mask >> 16) & 0xFF, (mask >> 8) & 0xFF, mask & 0xFF);
	}


	public static Optional<IllegalArgumentException> validateCIDR(int cidr) {
		if (cidr < 0 || cidr > 32) {
			return Optional.of(
					new IllegalArgumentException("CIDR value must be between 0 and 32 but was " + cidr));
		}
		return Optional.empty();
	}


	/**
	 * Returns the ip as a string formatted as "xxx.xxx.xxx.xxx"
	 *
	 * @return
	 */
	@Override
	public @NotNull String toString() {
		return Arrays.stream(ip)//
				.mapToObj(String::valueOf)//
				.collect(Collectors.joining("."));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		IPv4 ipv4 = (IPv4) o;
		return Arrays.equals(ip, ipv4.ip);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(ip);
	}
}
