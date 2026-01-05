package pl.edu.agh.po.service;

import pl.edu.agh.po.model.User;

public interface TotpManager {
    User setupBootstrap(User user, String newPassword);
    String generateOtpAuthUrl(User user);
    boolean isTotpCodeValid(User user, String code);
}