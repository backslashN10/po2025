package pl.edu.agh.po;

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
    public enum AuthStatus {
        SUCCESS,
        BOOTSTRAP_REQUIRED,
        PASSWORD_CHANGE_REQUIRED,
        TOTP_REQUIRED,
        INVALID_CREDENTIALS
    }

    public AuthStatus login(String username, String password) {
        User userLookup = userDAO.findByUsername(username);

        if (userLookup == null || !PasswordEncryption.verify(password, userLookup.getPassword())) {
            return AuthStatus.INVALID_CREDENTIALS;
        }
        currentUser = userLookup;

        if (userLookup.isBootstrap()) {
            return AuthStatus.BOOTSTRAP_REQUIRED;
        }

        if (userLookup.isForcePasswordChange()) {
            return AuthStatus.PASSWORD_CHANGE_REQUIRED;
        }
        if (userLookup.isForceTotpSetup()) {
            currentUser =  userLookup;
            return AuthStatus.TOTP_REQUIRED; // wymusza konfigurację przy pierwszym logowaniu
        }

        if (userLookup.isTotpEnabled()) {
            currentUser = userLookup;
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



