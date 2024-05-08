package com.EcomUserService.EcomUserService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "ECOM_USER")
public class User extends BaseClass{
    private String name;
    private String email;
    private String password;
    @ManyToMany
    private Set<Role> roles = new HashSet<>();
}
