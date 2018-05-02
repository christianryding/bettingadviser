package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bettingadviser.enums.SPORT_IDS;

class sportIDsTest {

	SPORT_IDS classUnderTest;
	
	@BeforeEach
	void setUp() throws Exception {
		classUnderTest = new SPORT_IDS();
	}

	/**
	 * Test so different sports give right id number
	 */
	@Test
	void testSportIDs() {
		assertEquals(33, classUnderTest.TENNIS);
		assertEquals(29, classUnderTest.SOCCER);
		assertEquals(12, classUnderTest.ESPORT);
		assertEquals(3, classUnderTest.BASEBALL);
		assertEquals(4, classUnderTest.BASKETBALL);
		assertEquals(19, classUnderTest.HOCKEY);
		assertEquals(18, classUnderTest.HANDBALL);
		assertEquals(28, classUnderTest.SNOOKER);
		assertEquals(15, classUnderTest.FOOTBALL);
	}

}
