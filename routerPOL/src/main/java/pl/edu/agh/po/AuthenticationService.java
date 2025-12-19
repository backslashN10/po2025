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
}

