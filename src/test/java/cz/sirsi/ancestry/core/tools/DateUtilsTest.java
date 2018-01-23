package cz.sirsi.ancestry.core.tools;

import junit.framework.TestCase;

public class DateUtilsTest extends TestCase {

	public DateUtilsTest(String name) {
		super(name);
	}

	public void testCompareTwoDates() {
		int result = DateUtils.compareTwoDates(DateUtils.parse("1.1.2009"), DateUtils.parse("1.2.2009"));
		assertTrue(result == -1);

		result = DateUtils.compareTwoDates(DateUtils.parse("1.2.2009"), DateUtils.parse("1.1.2009"));
		assertTrue(result == 1);

		result = DateUtils.compareTwoDates(DateUtils.parse("1.1.2009"), DateUtils.parse("1.1.2009"));
		assertTrue(result == 0);

		// --

		result = DateUtils.compareTwoDates(DateUtils.parse("1.1.2009"), DateUtils.parse("1.2009"));
		assertTrue(result == -1);

		result = DateUtils.compareTwoDates(DateUtils.parse("1.2009"), DateUtils.parse("1.1.2009"));
		assertTrue(result == 1);

		result = DateUtils.compareTwoDates(DateUtils.parse("31.1.2009"), DateUtils.parse("1.2009"));
		assertTrue(result == 1);

		result = DateUtils.compareTwoDates(DateUtils.parse("1.2009"), DateUtils.parse("31.1.2009"));
		assertTrue(result == -1);

		// --

		result = DateUtils.compareTwoDates(DateUtils.parse("1.2009"), DateUtils.parse("2.2009"));
		assertTrue(result == -1);

		result = DateUtils.compareTwoDates(DateUtils.parse("2.2009"), DateUtils.parse("1.2009"));
		assertTrue(result == 1);

		result = DateUtils.compareTwoDates(DateUtils.parse("1.2009"), DateUtils.parse("1.2009"));
		assertTrue(result == 0);

		// --

		result = DateUtils.compareTwoDates(DateUtils.parse("1.2009"), DateUtils.parse("2009"));
		assertTrue(result == -1);

		result = DateUtils.compareTwoDates(DateUtils.parse("2009"), DateUtils.parse("1.2009"));
		assertTrue(result == 1);

		result = DateUtils.compareTwoDates(DateUtils.parse("12.2009"), DateUtils.parse("2009"));
		assertTrue(result == 1);

		result = DateUtils.compareTwoDates(DateUtils.parse("2009"), DateUtils.parse("12.2009"));
		assertTrue(result == -1);

		// --

		result = DateUtils.compareTwoDates(DateUtils.parse("2009"), DateUtils.parse("2010"));
		assertTrue(result == -1);

		result = DateUtils.compareTwoDates(DateUtils.parse("2010"), DateUtils.parse("2009"));
		assertTrue(result == 1);

		result = DateUtils.compareTwoDates(DateUtils.parse("2009"), DateUtils.parse("2009"));
		assertTrue(result == 0);
	}
}
