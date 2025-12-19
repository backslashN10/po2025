package pl.edu.agh.po;

public class User {
    private long id;
    private String login;
    private String password;
    private UserRole role;

    public User(Long id, String login, String password, UserRole role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
