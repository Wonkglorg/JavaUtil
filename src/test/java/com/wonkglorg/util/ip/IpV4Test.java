package com.wonkglorg.util.ip;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class IpV4Test {
	private final String validV4Ip = "255.10.2.3";

	@Test
	void testValidIPv4() {
		IPv4 ipFromInts = new IPv4(255, 10, 2, 3);
		assertEquals(validV4Ip, ipFromInts.toString());

		IPv4 ipFromArrays = new IPv4(new int[]{255, 10, 2, 3});
		assertEquals(validV4Ip, ipFromArrays.toString());

		IPv4 ipFromString = new IPv4(validV4Ip);
		assertEquals(validV4Ip, ipFromString.toString());
	}

	@Test
	void testMalformationsThrow() {
		assertThrows(MalformedIpException.class, () -> new IPv4((String) null),
				"IP cannot be null or blank");
		assertThrows(MalformedIpException.class, () -> new IPv4(""), "IP cannot be null or blank");
		assertThrows(MalformedIpException.class, () -> new IPv4("1.30.1.3."), "Malformed Ip");
		assertThrows(MalformedIpException.class, () -> new IPv4("1..."));
		assertThrows(RuntimeException.class, () -> new IPv4("344.1.3.3"));
		assertThrows(RuntimeException.class, () -> new IPv4(344, 1, 3, 3));
		assertThrows(RuntimeException.class, () -> new IPv4(new int[]{344, 344, 1}));
	}


	@Test
	void testNetworkAddress() {
		IPv4 ip = new IPv4(192, 168, 1, 10);
		IPv4 subnetMask = new IPv4(255, 255, 255, 0);
		IPv4 expectedNetworkAddress = new IPv4(192, 168, 1, 0);

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
		IPv4 ip = new IPv4(192, 168, 1, 10);
		IPv4 subnetMask = new IPv4(255, 255, 255, 0);
		IPv4 expectedBroadcastAddress = new IPv4(192, 168, 1, 255);

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
