package pl.edu.agh.po;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuthenticationService{
    private static AuthenticationService instance;
    private User currentUser;
    private UserDAO userDAO;

    private AuthenticationService(){
        this.userDAO = UserDAO.getInstance();
        this.currentUser = null;
    }
    public static AuthenticationService getInstance(){
        if (instance == null){
            instance = new AuthenticationService();
        }
        return instance;
    }
    public boolean login(String username, String password){
        User userLookup = UserDAO.getInstance().findByUsername(username);
        if (userLookup != null){
            if (PasswordEncryption.verify(password, userLookup.getPassword())){
                currentUser = userLookup;
                return true;
            }
        }
        return false;
    }


    public void logout(){
        currentUser = null;
    }
    public User getCurrentUser(){
        return currentUser;
    }
}

