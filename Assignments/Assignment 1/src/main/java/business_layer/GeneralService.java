package business_layer;

import data_access_layer.IUserDAO;
import data_access_layer.UserDAO;
import exceptions.InexistentAccountException;
import exceptions.InvalidPasswordException;
import exceptions.UsernameAlreadyExistsException;
import model.User;

import java.util.List;

public class GeneralService implements IGeneralService {

    protected IUserDAO userDAO;

    public GeneralService() {
        this.userDAO = new UserDAO();
    }

    public User signUp(User user)
            throws UsernameAlreadyExistsException {

        User duplicateUser = userDAO.getUserByUsername(user.getUsername());

        if (duplicateUser != null)
            throw new UsernameAlreadyExistsException("Username already exists");

        boolean saved = userDAO.save(user);
        if (saved) {
            return userDAO.getUserByUsername(user.getUsername());
        }
        return null;
    }


    public User logIn(String username, String password) throws InvalidPasswordException, InexistentAccountException {

        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            throw new InexistentAccountException("Invalid username");
        }

        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new InvalidPasswordException("Invalid password");
        }
    }

    public User changePassword(String username, String oldPassword, String newPassword)
            throws InvalidPasswordException, InexistentAccountException {

        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            throw new InexistentAccountException("Invalid username");
        }

        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            boolean updated = userDAO.update(user);
        } else {
            throw new InvalidPasswordException("Invalid old password");
        }
        return user;
    }

    public User getUserByUsername(String username) {

        return userDAO.getUserByUsername(username);
    }

    public List<User> getUsers() {
        return userDAO.getUsers();
    }

}
