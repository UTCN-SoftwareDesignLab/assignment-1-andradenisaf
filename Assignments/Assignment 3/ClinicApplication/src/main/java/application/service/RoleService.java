package application.service;

import application.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        Iterable<Role> iterable = roleRepository.findAll();
        ArrayList<Role> roles = new ArrayList<>();
        iterable.forEach(roles::add);
        return roles;
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        Role role;
        try {
            role = optionalRole.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return role;
    }

    @Override
    public Role findByType(String type) {
        Optional<Role> optionalRole = roleRepository.findByType(type);
        Role role;
        try {
            role = optionalRole.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return role;
    }
}

