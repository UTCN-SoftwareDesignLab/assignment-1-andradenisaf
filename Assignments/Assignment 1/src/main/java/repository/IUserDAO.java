package repository;

import model.User;

import java.util.List;

public interface IUserDAO {

    public boolean save(User user);

    public boolean delete(String username);

    public boolean update(User user);

    public User getUserByUsername(String username);

    public List<User> getUsers();

}
