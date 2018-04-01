package business_layer;

import exceptions.InexistentAccountException;
import exceptions.InvalidPasswordException;
import exceptions.UsernameAlreadyExistsException;
import model.User;

import java.util.List;

public interface IGeneralService {

    public User signUp(User user)
            throws UsernameAlreadyExistsException;

    public User logIn(String username, String password) throws InvalidPasswordException, InexistentAccountException;

    public User changePassword(String username, String oldPassword, String newPassword)
            throws InvalidPasswordException, InexistentAccountException;

    public List<User> getUsers();

    public User getUserByUsername(String username) throws InexistentAccountException;

}
