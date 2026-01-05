package pl.edu.agh.po;

import javafx.scene.control.Alert;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static UserDAO instance;
    private static Connection connection;

    private UserDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:rp.db");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    private void createBootstrapAdmin() {

        User admin = new User(
                0L,
                "admin1",
                "admin1",
                UserRole.ADMIN
        );

        admin.setBootstrap(true);
        admin.setForcePasswordChange(true);
        admin.setTotpEnabled(false);

        save(admin);
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL,
                bootstrap INTEGER DEFAULT 0,
                force_password_change INTEGER DEFAULT 0,
                totp_secret TEXT,
                totp_enabled INTEGER DEFAULT 0,
                force_totp_setup INTEGER DEFAULT 1
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            boolean isAdminExists = false;
            try (ResultSet rs = stmt.executeQuery("SELECT 1 FROM users WHERE username = 'admin1'")) {
                if (rs.next()) {
                    isAdminExists = true;
                }
            }
            if (!isAdminExists) {
                createBootstrapAdmin();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public User findByID(long ID) {
//        String sql = "SELECT * FROM users WHERE id = ?";
//        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            pstmt.setLong(1, ID);
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                User user = new User(
//                        rs.getLong("id"),
//                        rs.getString("username"),
//                        rs.getString("password"),
//                        UserRole.valueOf(rs.getString("role"))
//                );
//
//                user.setBootstrap(rs.getInt("bootstrap") == 1);
//                user.setForcePasswordChange(rs.getInt("force_password_change") == 1);
//                user.setTotpSecret(rs.getString("totp_secret"));
//                user.setTotpEnabled(rs.getInt("totp_enabled") == 1);
//                user.setForceTotpSetup(rs.getInt("force_totp_setup") == 1);
//
//                return user;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRole.valueOf(rs.getString("role"))
                );

                user.setBootstrap(rs.getInt("bootstrap") == 1);
                user.setForcePasswordChange(rs.getInt("force_password_change") == 1);
                user.setTotpSecret(rs.getString("totp_secret"));
                user.setTotpEnabled(rs.getInt("totp_enabled") == 1);
                user.setForceTotpSetup(rs.getInt("force_totp_setup") == 1);

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public User findByRole(UserRole role) {
//        String sql = "SELECT * FROM users WHERE role = ?";
//        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            pstmt.setString(1, role.name());
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                User user = new User(
//                        rs.getLong("id"),
//                        rs.getString("username"),
//                        rs.getString("password"),
//                        UserRole.valueOf(rs.getString("role"))
//                );
//
//                user.setBootstrap(rs.getInt("bootstrap") == 1);
//                user.setForcePasswordChange(rs.getInt("force_password_change") == 1);
//                user.setTotpSecret(rs.getString("totp_secret"));
//                user.setTotpEnabled(rs.getInt("totp_enabled") == 1);
//                user.setForceTotpSetup(rs.getInt("force_totp_setup") == 1);
//
//                return user;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<User> findALL() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next() == true) {
                users.add(new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"), UserRole.valueOf(rs.getString("role"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (username, password, role, bootstrap, force_password_change, totp_secret, totp_enabled,force_totp_setup) VALUES (?,?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, PasswordEncryption.hash(user.getPassword()));
            pstmt.setString(3, user.getRole().name());
            pstmt.setInt(4, user.isBootstrap() ? 1 : 0);
            pstmt.setInt(5, user.isForcePasswordChange() ? 1 : 0);
            pstmt.setString(6, user.getTotpSecret());
            pstmt.setInt(7, user.isTotpEnabled() ? 1 : 0);
            pstmt.setInt(8, user.isForceTotpSetup() ? 1 : 0);

            pstmt.executeUpdate();

            // Pobranie wygenerowanego ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                throw new UserAlreadyExistsException("User already exists", e);
            }
            throw new RuntimeException(e);

        }
    }

    public void deleteByID(long ID) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, ID);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateData(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, bootstrap = ?, force_password_change = ?, totp_secret = ?, totp_enabled = ?,force_totp_setup = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, PasswordEncryption.hash(user.getPassword()));
                pstmt.setString(3, user.getRole().name());
                pstmt.setInt(4, user.isBootstrap() ? 1 : 0);
                pstmt.setInt(5, user.isForcePasswordChange() ? 1 : 0);
                pstmt.setString(6, user.getTotpSecret());
                pstmt.setInt(7, user.isTotpEnabled() ? 1 : 0);
                pstmt.setInt(8, user.isForceTotpSetup() ? 1 : 0);
                pstmt.setLong(9, user.getId());

                pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

