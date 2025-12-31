package lab3;

import static org.junit.Assert.*;

import org.junit.Test;

public class CodingBatTest {

	@Test
	public void testSleepIn() {
		CodingBat cb = new CodingBat();
		assertTrue(cb.sleepIn(false, false));
		assertFalse(cb.sleepIn(true, false));
		assertTrue(cb.sleepIn(false, true));
		assertTrue(cb.sleepIn(true, true));
	}

	@Test
	public void testDiff21() {
		CodingBat cb = new CodingBat();
		assertEquals(2, cb.diff21(19));
		assertEquals(11, cb.diff21(10));
		assertEquals(0, cb.diff21(21));
		assertEquals(2, cb.diff21(22));
	}
	@Test
	public void helloName() {
		CodingBat cb = new CodingBat();
		assertEquals("Hello Bob!", cb.helloName("Bob"));
		assertEquals("Hello Alice!", cb.helloName("Alice"));
		assertEquals("Hello X!", cb.helloName("X"));
	}
	@Test	
	public void countEvens() {
		CodingBat cb = new CodingBat();
		assertEquals(3, cb.countEvens(new int[] {2, 1, 2, 3, 4}));
		assertEquals(3, cb.countEvens(new int[]{2, 2, 0}))	;
		assertEquals(0, cb.countEvens(new int[]{1, 3, 5}));
	}
}
