package com.wonkglorg.util.ip;

import com.wonkglorg.util.string.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents an IPv6
 */
@SuppressWarnings("unused")
public class IPv6 implements IP {
	private String[] ip = new String[8];

	private static final String ipErrorMessage = "Invalid IP format";

	/**
	 * Creates a new ip from a string formatted as any valid IPv6
	 *
	 * @param ip the ip to convert
	 * @throws IllegalArgumentException if the ip is not in the correct format
	 */
	public IPv6(String ip) {

		if (!isValidIP(ip)) {
			throw new IllegalArgumentException(ipErrorMessage);
		}

		this.ip = seperateIpParts(ip);
	}

	/**
	 * Creates a new ip from its 8 parts
	 *
	 * @throws IllegalArgumentException if the ip has not 8 parts or a part is not valid
	 */
	public IPv6(String part1, String part2, String part3, String part4, String part5, String part6,
			String part7, String part8) {
		this(new String[]{part1, part2, part3, part4, part5, part6, part7, part8});
	}

	/**
	 * Creates a new ip from its 8 parts
	 *
	 * @param ip parts of the ip
	 * @throws IllegalArgumentException if the ip has not 8 parts or a part is not valid
	 */
	public IPv6(String[] ip) {
		if (ip.length != 8) {
			throw new IllegalArgumentException(ipErrorMessage);
		}
		for (String part : ip) {
			if (!isValidSegment(part)) {
				throw new IllegalArgumentException(ipErrorMessage);
			}
		}
		this.ip = ip;
	}

	/**
	 * Seperates a valid ip into its 8 parts adding leading zeros if necessary and unshortened
	 *
	 * @param ip
	 * @return
	 */
	private String[] seperateIpParts(String ip) {
		String[] ipResult = new String[8];
		String[] ipParts = ip.split(":");
		int length = ipParts.length;
		for (int i = 0; i < length; i++) {
			if (ipParts[i].isEmpty()) {
				int emptyParts = 8 - length + 1;
				for (int j = 0; j < emptyParts; j++) {
					ipResult[i + j] = "0000";
				}
				continue;
			}
			ipResult[i] = StringUtils.padLeft(ipParts[i], 4, '0');
		}
		return ipResult;
	}

	/**
	 * Checks if the segment is valid
	 *
	 * @param part segment to check
	 * @return true if the segment is valid
	 */
	private static boolean isValidSegment(String part) {
		if (part.length() > 4) {
			System.out.println("\"" + part + "\" cannot have more than 4 digits");
			return false;
		}
		for (char c : part.toCharArray()) {
			if (!Character.isDigit(c) && !Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the ip has a valid format
	 *
	 * @param ip
	 * @return
	 */
	public static boolean isValidIP(String ip) {
		String[] parts = ip.split(":");
		boolean hasDoubleColon = false;

		// Check if the ip has at least 2 parts otherwise its invalid
		if (parts.length <= 1) {
			return false;
		}

		// Check if the ip has less than 8 parts otherwise its invalid
		if (parts.length > 8) {
			return false;
		}

		//checks if only one double colon is present
		for (String part : parts) {
			if (part.isEmpty()) {
				if (hasDoubleColon) {
					return false;
				}
				hasDoubleColon = true;
				continue;
			}
			// Check if the part is valid
			isValidSegment(part);
		}

		return true;
	}

	/**
	 * @return returns a shortened IPv6 with leading zeros removed and the longest sequence of zeros
	 * shortened to "::"
	 */
	public String toShortenedIP() {
		String[] ipParts = ip;
		int longestZeroSequence = 0;
		int currentZeroSequence = 0;
		int longestZeroSequenceIndex = 0;
		int currentZeroSequenceIndex = 0;
		ipParts = Arrays.stream(ipParts).map(part -> StringUtils.removeAllLeading(part, '0'))
				.toArray(String[]::new);
		for (int i = 0; i < ipParts.length; i++) {
			if (ipParts[i] == null || ipParts[i].equals("")) {
				currentZeroSequence++;
				if (currentZeroSequence > longestZeroSequence) {
					longestZeroSequence = currentZeroSequence;
					longestZeroSequenceIndex = currentZeroSequenceIndex;
				}
			} else {
				currentZeroSequence = 0;
				currentZeroSequenceIndex = i;
			}
		}
		if (longestZeroSequence > 1) {
			ipParts[longestZeroSequenceIndex] = "";
			for (int i = 1; i < longestZeroSequence; i++) {
				ipParts[longestZeroSequenceIndex + i] = "";
			}
		}
		return String.join(":", ipParts);
	}

	/**
	 * @return ip formatted as "xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx"
	 */
	@Override
	public String toString() {
		return String.join(":", ip);
	}

	@Override
	public Optional<MalformedIpException> validateIp(String ip) {
		return Optional.empty();
	}
}
