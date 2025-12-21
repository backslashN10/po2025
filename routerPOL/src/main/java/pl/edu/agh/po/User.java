package pl.edu.agh.po;

public class User {
    private long id;
    private String username;
    private String password;
    private UserRole role;

    public User(Long id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    // dla tworzenia nowych userow
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public long getID(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public UserRole getRole(){
        return role;
    }
    public void setID(long id) {
        this.id = id;
    }
}
