package pl.edu.agh.po.service;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import org.apache.commons.codec.binary.Base32;
import pl.edu.agh.po.dao.UserDAO;
import pl.edu.agh.po.model.User;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

public class TotpService
{
    private final UserDAO userDAO = UserDAO.getInstance();

    public User setupBootstrap(User user, String newPassword) {
        user.setPassword(newPassword);
        user.setForcePasswordChange(false);
        user.setBootstrap(false);

        // TOTP
        SecretKey secretKey = null;
        try {
            secretKey = KeyGenerator.getInstance("HmacSHA1").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String base32Secret = new Base32().encodeToString(secretKey.getEncoded()).replace("=", "");
        user.setTotpSecret(base32Secret);
        user.setTotpEnabled(true);

        userDAO.updateData(user); // update w DB
        return user;
    }

    public String generateOtpAuthUrl(User user) {
        return "otpauth://totp/YourApp:" + user.getUsername() +
                "?secret=" + user.getTotpSecret() +
                "&issuer=YourApp&digits=6";
    }
    public boolean isTotpCodeValid(User user, String codeInput) {
        try {
            String secret = user.getTotpSecret();
            if (secret == null || secret.isBlank()) return false;

            byte[] keyBytes = new Base32().decode(secret);
            SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");

            TimeBasedOneTimePasswordGenerator totp =
                    new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));

            Instant now = Instant.now();
            for (int i = -1; i <= 1; i++) { // ±1 krok
                Instant t = now.plusSeconds(i * 30);
                String expected = String.format("%06d", totp.generateOneTimePassword(key, t));
                if (expected.equals(codeInput)) return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }
    public void setupTotp(User user) throws Exception {
        if (user.getTotpSecret() == null || user.getTotpSecret().isBlank()) {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA1");
            keyGen.init(160);
            SecretKey secretKey = keyGen.generateKey();

            String base32Secret = new Base32().encodeToString(secretKey.getEncoded());
            user.setTotpSecret(base32Secret);
            user.setTotpEnabled(false);

            userDAO.updateData(user); // zapis w DB
        }
    }


}
