package pl.edu.agh.po.service;

import pl.edu.agh.po.utilities.PasswordEncryption;
import pl.edu.agh.po.dao.UserDAO;
import pl.edu.agh.po.model.User;
import pl.edu.agh.po.model.AuthStatus;


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


    public AuthStatus login(String username, String password) {
        User userLookup = userDAO.findByUsername(username);
        currentUser = userLookup;


        if (userLookup == null || !PasswordEncryption.verify(password, userLookup.getPassword())) {
            return AuthStatus.INVALID_CREDENTIALS;
        }

        if (userLookup.isBootstrap()) {
            return AuthStatus.BOOTSTRAP_REQUIRED;
        }
        if (userLookup.isForcePasswordChange()) {
            return AuthStatus.PASSWORD_CHANGE_REQUIRED;
        }
        if (userLookup.isForceTotpSetup()) {
            return AuthStatus.TOTP_REQUIRED;
        }
        if (userLookup.isTotpEnabled()) {
            return AuthStatus.TOTP_REQUIRED;
        }
        return AuthStatus.SUCCESS;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}



