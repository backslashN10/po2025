package pl.edu.agh.po.service;

import pl.edu.agh.po.dao.UserDAO;
import pl.edu.agh.po.exceptions.UserAlreadyExistsException;
import pl.edu.agh.po.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService implements UserManager{

    private final UserDAO userDAO = UserDAO.getInstance();
    private static UserService instance;

    public static UserService getInstance(){
        if (instance == null){
            instance = new UserService();
        }
        return instance;
    }

    public void createUser(User user) {
        try {
            userDAO.save(user);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SQLException
                    && e.getCause().getMessage().contains("UNIQUE")) {
                throw new UserAlreadyExistsException("User already exists", e);
            }
            throw e;
        }
    }

    public void deleteUserByUsername(String username) {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username); // opcjonalnie własny wyjątek
        }
        try {
            userDAO.deleteByID(user.getId());
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public List<User> getAllUsers() {
        return userDAO.findALL();
    }

    public User getByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public void update(User user) {
        try {
            userDAO.updateData(user);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
