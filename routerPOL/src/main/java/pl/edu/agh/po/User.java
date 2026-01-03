package pl.edu.agh.po;

public class User {
    private long id;
    private String username;
    private String password;
    private UserRole role;

    private boolean bootstrap;
    private boolean forcePasswordChange;
    private String totpSecret;
    private boolean totpEnabled;

    public User(long id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.bootstrap = false;
        this.forcePasswordChange = false;
        this.totpEnabled = false;
        this.totpSecret = null;
    }
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.bootstrap = false;
        this.forcePasswordChange = false;
        this.totpEnabled = false;
        this.totpSecret = null;
    }
    public long getId(){
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
    public void setPassword(String password){this.password = password;}
    public boolean isBootstrap() { return bootstrap; }
    public void setBootstrap(boolean bootstrap) { this.bootstrap = bootstrap; }
    public boolean isForcePasswordChange() { return forcePasswordChange; }
    public void setForcePasswordChange(boolean forcePasswordChange) { this.forcePasswordChange = forcePasswordChange; }
    public String getTotpSecret() { return totpSecret; }
    public void setTotpSecret(String totpSecret) { this.totpSecret = totpSecret; }
    public boolean isTotpEnabled() { return totpEnabled; }
    public void setTotpEnabled(boolean totpEnabled) { this.totpEnabled = totpEnabled; }
    public void setId(long id) {this.id=id;}
}
