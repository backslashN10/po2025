package pl.edu.agh.po;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.SecureRandom;

public class UserDAO {
    private static UserDAO instance;
    private static Connection connection;
    
    private UserDAO(){
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:rp.db");
            //create random admin login password first use
            createTable();
            createDefaultAdminIfNotExists();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }



    private void createDefaultAdminIfNotExists() {
        if (isAdminExists()) {
            return;
        }

        String password = generateRandomPassword(12);

        User admin = new User(
                1L,
                "admin",
                password,
                UserRole.ADMIN
        );

        save(admin);

        System.out.println("=================================");
        System.out.println("UTWORZONO KONTO ADMINA");
        System.out.println("login: admin");
        System.out.println("hasło: " + password);
        System.out.println("=================================");
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private boolean isAdminExists() {
        String sql = "SELECT 1 FROM users WHERE role = 'ADMIN' LIMIT 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static UserDAO getInstance(){
        if (instance == null){
            instance = new UserDAO();
        }
        return instance;
    }
    private void createTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL
                );
                """;
        try (Statement stmt = connection.createStatement()){
            stmt.execute(sql);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public User findByUsername(String username){
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() == true){
                return new User(rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public void save(User user){
        String sql = "INSERT INTO users ('username', 'password', 'role') VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, PasswordEncryption.hash(user.getPassword()));
            pstmt.setString(3, user.getRole().name());
            pstmt.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
