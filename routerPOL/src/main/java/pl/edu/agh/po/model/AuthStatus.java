package pl.edu.agh.po.model;

public enum AuthStatus {
    SUCCESS,
    BOOTSTRAP_REQUIRED,
    PASSWORD_CHANGE_REQUIRED,
    TOTP_REQUIRED,
    INVALID_CREDENTIALS
}
