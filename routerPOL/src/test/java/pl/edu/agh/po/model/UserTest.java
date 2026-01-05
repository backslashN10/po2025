package pl.edu.agh.po.model;

import org.junit.jupiter.api.Test; // JUnit 5
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User("xyz", "xyz", UserRole.ADMIN);
        assertEquals("xyz", user.getUsername());
        assertEquals("xyz", user.getPassword());
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void testForceTotpSetupFlag() {
        User user = new User("xyz", "xyz", UserRole.TECHNICIAN);
        assertFalse(user.isForceTotpSetup());
        user.setForceTotpSetup(true);
        assertTrue(user.isForceTotpSetup());
    }

    @Test
    void testBootstrapFlag() {
        User user = new User("admin1", "admin1", UserRole.ADMIN);
        user.setBootstrap(true);
        assertTrue(user.isBootstrap());
    }

    @Test
    void testTotpSecret() {
        User user = new User("ceo", "pwd", UserRole.CEO);
        user.setTotpSecret("123");
        assertEquals("123", user.getTotpSecret());
    }
}