package com.wonkglorg.util.ip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IpV6Test {
	private final String textV6Ip = "2001:0DB8:AC10:FE01:0000:0000:0000:0000";

	@Test
	void testValidIPV6() {
		IPv6 ipFromString = new IPv6(textV6Ip);
		Assertions.assertEquals(textV6Ip, ipFromString.toString());

		IPv6 ipFromStringParts =
				new IPv6("2001", "0DB8", "AC10", "FE01", "0000", "0000", "0000", "0000");
		Assertions.assertEquals(textV6Ip, ipFromStringParts.toString());

		IPv6 ipFromArrays =
				new IPv6(new String[]{"2001", "0DB8", "AC10", "FE01", "0000", "0000", "0000", "0000"});
		Assertions.assertEquals(textV6Ip, ipFromArrays.toString());

	}
}
