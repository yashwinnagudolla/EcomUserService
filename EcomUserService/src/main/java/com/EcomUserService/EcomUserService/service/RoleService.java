package com.EcomUserService.EcomUserService.service;

import com.EcomUserService.EcomUserService.model.Role;
import com.EcomUserService.EcomUserService.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }

    public Set<Role> findAllByRoleIn(List<String> roles) {
        return roleRepository.findAllByRoleIn(roles).get();
    }

    public Role createRole(String name){
        Role role = new Role();
        role.setRole(name);
        return roleRepository.save(role);
    }
}
