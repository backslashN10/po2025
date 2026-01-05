package pl.edu.agh.po.service;

import pl.edu.agh.po.model.User;
import pl.edu.agh.po.exceptions.UserAlreadyExistsException;

import java.util.List;

public interface UserManager {
    void createUser(User user) throws UserAlreadyExistsException;
    void update(User user);
    void deleteUserByUsername(String username);
    List<User> getAllUsers();
}
