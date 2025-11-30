package po;

import static org.junit.Assert.*;
import org.junit.Test;

public class PozycjaTest {

    @Test
    public void testPrzemiescDoNowejPozycji() {
        Pozycja start = new Pozycja(0.0, 0.0);
        Pozycja koniec = new Pozycja(-10.0, 20.0);

        double predkosc = 5.0;
        double dt = 0.1;

        start.przemiesc(koniec, predkosc, dt);

        // Użyj assertEquals z tolerancją zamiast ==
        assertEquals("X powinno byc rowne -10.0", -10.0, start.getX(), 0.01);
        assertEquals("Y powinno byc rowne 20.0", 20.0, start.getY(), 0.01);
    }
}