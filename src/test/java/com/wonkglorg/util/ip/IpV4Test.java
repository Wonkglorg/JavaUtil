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
		IPv4 ip = IPv4.of(192, 168, 1, 10);
		IPv4 subnetMask = IPv4.of(255, 255, 255, 0);
		IPv4 expectedNetworkAddress = IPv4.of(192, 168, 1, 0);

		int cidr = 24;

		//check for null inputs
		assertThrows(RuntimeException.class, () -> IPv4.getNetworkAddress(null, null));
		assertThrows(RuntimeException.class, () -> IPv4.getNetworkAddress(ip, null));
		assertThrows(RuntimeException.class, () -> IPv4.getNetworkAddress(null, subnetMask));


		//valid calculations check
		assertEquals(expectedNetworkAddress, ip.getNetworkAddress(subnetMask));
		assertEquals(expectedNetworkAddress, ip.getNetworkAddress(cidr));
		assertEquals(expectedNetworkAddress, IPv4.getNetworkAddress(ip, subnetMask));
		assertEquals(expectedNetworkAddress, IPv4.getNetworkAddress(ip, cidr));

		//cidr bounds check
		assertThrows(IllegalArgumentException.class, () -> ip.getNetworkAddress(-1));
		assertThrows(IllegalArgumentException.class, () -> ip.getNetworkAddress(33));
	}

	@Test
	void testBroadcastAddress() {
		IPv4 ip = IPv4.of(192, 168, 1, 10);
		IPv4 subnetMask = IPv4.of(255, 255, 255, 0);
		IPv4 expectedBroadcastAddress = IPv4.of(192, 168, 1, 255);

		int cidr = 24;

		//check for null inputs
		assertThrows(RuntimeException.class, () -> IPv4.getBroadcastAddress(null, null));
		assertThrows(RuntimeException.class, () -> IPv4.getBroadcastAddress(ip, null));
		assertThrows(RuntimeException.class, () -> IPv4.getBroadcastAddress(null, subnetMask));

		//valid calculations check
		assertEquals(expectedBroadcastAddress, ip.getBroadcastAddress(subnetMask));
		assertEquals(expectedBroadcastAddress, ip.getBroadcastAddress(cidr));
		assertEquals(expectedBroadcastAddress, IPv4.getBroadcastAddress(ip, subnetMask));
		assertEquals(expectedBroadcastAddress, IPv4.getBroadcastAddress(ip, cidr));

		//cidr bounds check
		assertThrows(IllegalArgumentException.class, () -> ip.getBroadcastAddress(-1));
		assertThrows(IllegalArgumentException.class, () -> ip.getBroadcastAddress(33));
	}

}
