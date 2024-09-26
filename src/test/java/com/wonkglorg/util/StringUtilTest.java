package com.wonkglorg.util;

import com.wonkglorg.util.reflection.ReflectionDisplay;
import com.wonkglorg.util.string.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.wonkglorg.util.console.ConsoleUtil.println;

class StringUtilTest {

	@Test
	void doesPaddingLeftWork() {
		var result1 = StringUtils.padLeft("Test", 6);
		Assertions.assertEquals("  Test", result1);

		var result2 = StringUtils.padLeft("Test", 6, '-');
		Assertions.assertEquals("--Test", result2);

		var result3 = StringUtils.padLeft("Test", -6);
		Assertions.assertEquals("Test", result3);
	}

	@Test
	void doesPaddingRightWork() {
		var result1 = StringUtils.padRight("Test", 6);
		Assertions.assertEquals("Test  ", result1);

		var result2 = StringUtils.padRight("Test", 6, '-');
		Assertions.assertEquals("Test--", result2);

		var result3 = StringUtils.padRight("Test", -6);
		Assertions.assertEquals("Test", result3);
	}

	@Test
	void doesPaddingCenterWork() {
		var result1 = StringUtils.padCenter("Test", 6);
		Assertions.assertEquals(" Test ", result1);

		var result2 = StringUtils.padCenter("Test", 6, '-');
		Assertions.assertEquals("-Test-", result2);

		var result3 = StringUtils.padCenter("Test", -6);
		Assertions.assertEquals("Test", result3);
	}

	@Test
	void doesCapitalizeAllWordsWork() {
		var result1 = StringUtils.capitalizeAllWords("this is a lowercase sentence");
		Assertions.assertEquals("This Is A Lowercase Sentence", result1);

		var result2 = StringUtils.capitalizeAllWords("This Is Uppercase");
		Assertions.assertEquals("This Is Uppercase", result2);

		var result3 = StringUtils.capitalizeAllWords("");
		Assertions.assertNotNull(result3);

	}

	@Test
	void doesCountAllLeadingWork() {
		var result1 = StringUtils.countAllLeading("III", 'I');
		Assertions.assertEquals(3, result1);

		var result2 = StringUtils.countAllLeading("IIID", 'O');
		Assertions.assertEquals(0, result2);

		var result3 = StringUtils.countAllLeading("IIDI", 'I');
		Assertions.assertEquals(2, result3);

		var result4 = StringUtils.countAllLeading(null, 'I');
		Assertions.assertEquals(0, result4);
	}

	@Test
	void doesCountAllTrailingWork() {
		var result1 = StringUtils.countAllTrailing("III", 'I');
		Assertions.assertEquals(3, result1);

		var result2 = StringUtils.countAllTrailing("IIID", 'O');
		Assertions.assertEquals(0, result2);

		var result3 = StringUtils.countAllTrailing("IIDI", 'I');
		Assertions.assertEquals(1, result3);

		var result4 = StringUtils.countAllTrailing(null, 'I');
		Assertions.assertEquals(0, result4);
	}

	@Test
	void doesGenerateRandomStringWork() {
		var resul1 = StringUtils.generateRandomString(4);
		Assertions.assertEquals(4, resul1.length());

		var resul2 = StringUtils.generateRandomString(-4);
		Assertions.assertEquals(0, resul2.length());

		var resul3 = StringUtils.generateRandomString(4, "A");
		Assertions.assertEquals("AAAA", resul3);

		var resul4 = StringUtils.generateRandomString(-4, "A");
		Assertions.assertEquals("", resul4);
	}


	@Test
	void doesFormattingWork() {
		var result1 = StringUtils.format("This is a {0}", "Test");
		Assertions.assertEquals("This is a Test", result1);

		var result2 = StringUtils.format("This is a {0}, with the {0} number {1}", "Test", 1);
		Assertions.assertEquals("This is a Test, with the Test number 1", result2);

		var result3 = StringUtils.format("Invalid assertion {3} and valid null {0}", (Object) null);
		Assertions.assertEquals("Invalid assertion {3} and valid null ", result3);
	}

	@Test
	void doesXWork() {

	}

}
