package application.service;

import application.entity.Role;

import java.util.List;

public interface IRoleService {

    List<Role> findAll();
    Role findById(Long id);
    Role findByType(String type);
}
