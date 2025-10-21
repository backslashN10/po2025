package lab2;

import static org.junit.Assert.*;

import org.junit.Test;

public class CodingBatTest {

	@Test
	public void testNearHundred() {
		CodingBat cb = new CodingBat();

        // Bliskie 100
        assertTrue(cb.nearHundred(100));
        assertTrue(cb.nearHundred(90));
        assertTrue(cb.nearHundred(110));

        // Bliskie 200
        assertTrue(cb.nearHundred(200));
        assertTrue(cb.nearHundred(100));
        assertTrue(cb.nearHundred(210));

        // Daleko od 100 i 200
        assertFalse(cb.nearHundred(50));
        assertFalse(cb.nearHundred(180));
        assertFalse(cb.nearHundred(100));
	}
}
