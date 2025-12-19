package pl.edu.agh.po;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static UserDAO instance;
    private static Connection connection;
    
    private UserDAO(){
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:rp.db");
            createTable();
        }
        catch (SQLException e){
            e.printStackTrace();
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
                return new User(rs.getLong("id"), rs.getString("username"), rs.getString("password"), UserRole.valueOf(rs.getString("role")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
