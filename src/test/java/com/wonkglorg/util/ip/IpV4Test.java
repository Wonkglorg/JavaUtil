package com.wonkglorg.util.ip;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class IpV4Test {
	private final String validV4Ip = "255.10.2.3";

	@Test
	void testValidIPv4() {
		IPv4 ipFromInts = IPv4.of(255, 10, 2, 3);
		assertEquals(validV4Ip, ipFromInts.toString());

		IPv4 ipFromArrays = IPv4.of(new int[]{255, 10, 2, 3});
		assertEquals(validV4Ip, ipFromArrays.toString());

		IPv4 ipFromString = IPv4.of(validV4Ip);
		assertEquals(validV4Ip, ipFromString.toString());

		IPv4 smallestIp = IPv4.of("0.0.0.1");
		assertEquals(IPv4.Min, smallestIp);

		IPv4 maxIp = IPv4.of("255.255.255.255");
		assertEquals(IPv4.Max, maxIp);
	}

	@Test
	void testMalformationsThrow() {
		assertThrows(MalformedIpException.class, () -> IPv4.of((String) null),
				"IP cannot be null or blank");
		assertThrows(MalformedIpException.class, () -> IPv4.of(""), "IP cannot be null or blank");
		assertThrows(MalformedIpException.class, () -> IPv4.of("1.30.1.3."), "Malformed Ip");
		assertThrows(MalformedIpException.class, () -> IPv4.of("1..."));
		assertThrows(RuntimeException.class, () -> IPv4.of("344.1.3.3"));
		assertThrows(RuntimeException.class, () -> IPv4.of(344, 1, 3, 3));
		assertThrows(RuntimeException.class, () -> IPv4.of(new int[]{344, 344, 1}));
	}


	@Test
	void testNetworkAddress() {
		IPv4 validIp = IPv4.of(192, 168, 1, 10);
		IPv4 validSubnetMask = IPv4.of(255, 255, 255, 0);
		IPv4 expectedNetworkAddress = IPv4.of(192, 168, 1, 0);

		int validCidr = 24;

		//check for null inputs
		assertThrows(RuntimeException.class, () -> IPv4.getNetworkAddress(null, null));
		assertThrows(RuntimeException.class, () -> IPv4.getNetworkAddress(validIp, null));
		assertThrows(RuntimeException.class, () -> IPv4.getNetworkAddress(null, validSubnetMask));


		//valid calculations check
		assertEquals(expectedNetworkAddress, validIp.getNetworkAddress(validSubnetMask));
		assertEquals(expectedNetworkAddress, validIp.getNetworkAddress(validCidr));
		assertEquals(expectedNetworkAddress, IPv4.getNetworkAddress(validIp, validSubnetMask));
		assertEquals(expectedNetworkAddress, IPv4.getNetworkAddress(validIp, validCidr));

		//cidr bounds check
		assertThrows(IllegalArgumentException.class, () -> validIp.getNetworkAddress(-1));
		assertThrows(IllegalArgumentException.class, () -> validIp.getNetworkAddress(33));
	}

	@Test
	void testBroadcastAddress() {
		IPv4 validIp = IPv4.of(192, 168, 1, 10);
		IPv4 validSubnetMask = IPv4.of(255, 255, 255, 0);
		IPv4 expectedBroadcastAddress = IPv4.of(192, 168, 1, 255);

		int validCidr = 24;

		//check for null inputs
		assertThrows(RuntimeException.class, () -> IPv4.getBroadcastAddress(null, null));
		assertThrows(RuntimeException.class, () -> IPv4.getBroadcastAddress(validIp, null));
		assertThrows(RuntimeException.class, () -> IPv4.getBroadcastAddress(null, validSubnetMask));

		//valid calculations check
		assertEquals(expectedBroadcastAddress, validIp.getBroadcastAddress(validSubnetMask));
		assertEquals(expectedBroadcastAddress, validIp.getBroadcastAddress(validCidr));
		assertEquals(expectedBroadcastAddress, IPv4.getBroadcastAddress(validIp, validSubnetMask));
		assertEquals(expectedBroadcastAddress, IPv4.getBroadcastAddress(validIp, validCidr));

		//cidr bounds check
		assertThrows(IllegalArgumentException.class, () -> validIp.getBroadcastAddress(-1));
		assertThrows(IllegalArgumentException.class, () -> validIp.getBroadcastAddress(33));
	}

}
