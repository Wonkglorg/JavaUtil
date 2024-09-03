package com.wonkglorg.util.ip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class IpV6Test {

	@Test
	void testValidIPV6() {
		IPv6 validIp = IPv6.of("2001:0DB8:AC10:FE01:0000:0000:0000:0000");
		IPv6 ipFromString = IPv6.of("2001:0DB8:AC10:FE01:0000:0000:0000:0000");
		Assertions.assertEquals(validIp, ipFromString);

		IPv6 ipFromStringParts =
				IPv6.of("2001", "0DB8", "AC10", "FE01", "0000", "0000", "0000", "0000");
		Assertions.assertEquals(validIp, ipFromStringParts);

		IPv6 ipFromArrays =
				IPv6.of(new String[]{"2001", "0DB8", "AC10", "FE01", "0000", "0000", "0000", "0000"});
		Assertions.assertEquals(validIp, ipFromArrays);

		IPv6 smallestAddress = IPv6.of("::");
		Assertions.assertEquals(IPv6.Min, smallestAddress);

		IPv6 largestAddress = IPv6.of("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
		Assertions.assertEquals(IPv6.Max, largestAddress);

		IPv6 ipv6FromIpv4 = IPv6.fromIpv4(IPv4.of("1.1.1.1"));
		Assertions.assertEquals(IPv6.of("0000:0000:0000:ffff:0001:0001:0001:0001"), ipv6FromIpv4);


		//why does this evaluate to 0's only it keeps fffff till right the end
		IPv6 shortenedEnd = IPv6.of("::ffff");
		Assertions.assertEquals(IPv6.of("0000:0000:0000:0000:0000:0000:0000:ffff"), shortenedEnd);

		IPv6 shortenedStart = IPv6.of("ffff::");
		Assertions.assertEquals(IPv6.of("ffff:0000:0000:0000:0000:0000:0000:0000"), shortenedStart);

		IPv6 shortenedStart2Segment = IPv6.of("fFff:ffff::");
		Assertions.assertEquals(IPv6.of("ffff:ffff:0000:0000:0000:0000:0000:0000"),
				shortenedStart2Segment);


		IPv6 shortenedMiddle = IPv6.of("ffff::ffff");
		Assertions.assertEquals(IPv6.of("ffff:0000:0000:0000:0000:0000:0000:ffff"), shortenedMiddle);
	}

	@Test
	void formatting() {
		IPv6 validIp = IPv6.of("2001:0DB8:AC10:FE01:0000:0000:0000:0000");
		Assertions.assertEquals("2001:0db8:ac10:fe01:0000:0000:0000:0000", validIp.toString());

		Assertions.assertEquals("::", IPv6.Min.toShortenedIP());

		Assertions.assertEquals("0000:0000:0000:0000:0000:0000:0000:0000", IPv6.Min.toString());

		Assertions.assertEquals("0:20:1:ffff::", IPv6.of("0000:20:0001:ffff::").toShortenedIP());
		Assertions.assertEquals("0000:0020:0001:ffff:0000:0000:0000:0000",
				IPv6.of("0000:20:0001:ffff::").toString());

		Assertions.assertEquals("2001:db8:ac10:fe01::", validIp.toShortenedIP());
	}

	@Test
	void testMalformationsThrow() {
		assertThrows(MalformedIpException.class, () -> IPv6.of((String) null),
				"IP cannot be null or blank");
		assertThrows(MalformedIpException.class, () -> IPv6.of(":::"));
		assertThrows(MalformedIpException.class, () -> IPv6.of("eeee::"));
	}
}
