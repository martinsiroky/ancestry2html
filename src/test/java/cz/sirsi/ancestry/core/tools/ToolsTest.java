package cz.sirsi.ancestry.core.tools;

import java.util.Calendar;
import java.util.Locale;

import junit.framework.TestCase;

public class ToolsTest extends TestCase {

	public ToolsTest(String name) {
		super(name);
	}

	public void testCaledar() {
		System.out.println("SK:" + Calendar.getInstance(new Locale("sk", "SK")).getFirstDayOfWeek());
		System.out.println("GB:" + Calendar.getInstance(new Locale("en", "GB")).getFirstDayOfWeek());
		System.out.println("CZ:" + Calendar.getInstance(new Locale("cs", "CZ")).getFirstDayOfWeek());
		System.out.println("US:" + Calendar.getInstance(new Locale("en", "US")).getFirstDayOfWeek());
	}

	public void testCompareTwoVersions() {
		int result = Tools.compareTwoVersions("1.2.0.8", "1.2.0.8");
		assertTrue(result == 0);

		result = Tools.compareTwoVersions("1.2.0.8", "1.2.0.9");
		assertTrue(result == -1);
		result = Tools.compareTwoVersions("1.2.0.9", "1.2.0.8");
		assertTrue(result == 1);

		result = Tools.compareTwoVersions("1.2.1", "1.2.1.8");
		assertTrue(result == -1);
		result = Tools.compareTwoVersions("1.2.1.8", "1.2.1");
		assertTrue(result == 1);

		result = Tools.compareTwoVersions("1.2.1.8", "1.2.2");
		assertTrue(result == -1);
		result = Tools.compareTwoVersions("1.2.2", "1.2.1.8");
		assertTrue(result == 1);

		result = Tools.compareTwoVersions("1.2.0.9", "1.2.0.9b");
		assertTrue(result == -1);
		result = Tools.compareTwoVersions("1.2.0.9b", "1.2.0.9");
		assertTrue(result == 1);

		result = Tools.compareTwoVersions("1.2.0.9", "1.3.0.9");
		assertTrue(result == -1);
		result = Tools.compareTwoVersions("1.3.0.9", "1.2.0.9");
		assertTrue(result == 1);

		result = Tools.compareTwoVersions("2.3.0.9", "1.3.0.9");
		assertTrue(result == 1);
		result = Tools.compareTwoVersions("1.3.0.9", "2.3.0.9");
		assertTrue(result == -1);
	}
}
