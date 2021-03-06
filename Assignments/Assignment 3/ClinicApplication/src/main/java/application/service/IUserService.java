package application.service;

import application.entity.User;

import java.util.List;

public interface IUserService {

    List<User> findAll();
    User findById(Long id);
    User findByName(String name);
    User create(User user);
    User update(User user);
    boolean delete(User user);
}