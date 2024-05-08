package com.EcomUserService.EcomUserService.repository;

import com.EcomUserService.EcomUserService.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
    Optional<Set<Role>> findAllByIdIn(List<Long> roles);

    Optional<Set<Role>> findAllByRoleIn(List<String> roles);
}
