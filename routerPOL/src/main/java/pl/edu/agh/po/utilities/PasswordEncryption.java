package pl.edu.agh.po.utilities;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordEncryption
{
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean verify(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
