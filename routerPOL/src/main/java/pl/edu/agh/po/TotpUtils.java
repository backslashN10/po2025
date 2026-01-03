package pl.edu.agh.po;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

public class TotpUtils {

    // Tworzenie sekretu Base32 (SecretKey)
    public static SecretKey generateSecret() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
        keyGenerator.init(160); // standard TOTP = 160 bitów
        return keyGenerator.generateKey();
    }

    // Generowanie aktualnego kodu TOTP (do testów)
    public static String getCurrentCode(SecretKey key) throws Exception {
        TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));
        int code = totp.generateOneTimePassword(key, java.time.Instant.now());
        return String.format("%06d", code);
    }

    // Weryfikacja kodu wpisanego przez użytkownika
    public static boolean verifyCode(SecretKey key, String codeInput) throws Exception {
        String expected = getCurrentCode(key);
        return expected.equals(codeInput);
    }
}