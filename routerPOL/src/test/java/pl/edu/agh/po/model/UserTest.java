package pl.edu.agh.po.model;

import org.junit.jupiter.api.Test; // JUnit 5
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User("john", "secret", UserRole.ADMIN);
        assertEquals("john", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void testForceTotpSetupFlag() {
        User user = new User("jane", "pass", UserRole.TECHNICIAN);
        assertFalse(user.isForceTotpSetup());
        user.setForceTotpSetup(true);
        assertTrue(user.isForceTotpSetup());
    }

    @Test
    void testBootstrapFlag() {
        User user = new User("admin", "123", UserRole.ADMIN);
        user.setBootstrap(true);
        assertTrue(user.isBootstrap());
    }

    @Test
    void testTotpSecret() {
        User user = new User("ceo", "pwd", UserRole.CEO);
        user.setTotpSecret("ABC123");
        assertEquals("ABC123", user.getTotpSecret());
    }
}