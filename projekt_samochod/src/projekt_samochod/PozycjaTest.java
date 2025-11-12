package projekt_samochod;

import static org.junit.Assert.*;

import org.junit.Test;

public class PozycjaTest {

    @Test
    public void testPrzemiescDoNowejPozycji() {
        Pozycja start = new Pozycja(0.0, 0.0);
        Pozycja koniec = new Pozycja(-10.040, 20.0);

        double predkosc = 5.0;
        double dt = 0.1;

        start.przemiesc(koniec, predkosc, dt);

        assertTrue("X powinno byc rowne 10.0", start.getX() == -10.0);
        assertTrue("Y powinno byc rowne 10.0", start.getY() == 20.0);
        
    }
}
