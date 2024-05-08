package com.EcomUserService.EcomUserService.dto;

import com.EcomUserService.EcomUserService.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String email;
    private Set<Role> roles = new HashSet<>();

    public UserDTO(String email, Set<Role> roles) {
        this.email = email;
        this.roles = roles;
    }
}
