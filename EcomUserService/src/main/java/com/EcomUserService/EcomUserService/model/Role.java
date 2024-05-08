package com.EcomUserService.EcomUserService.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Role extends BaseClass {
    private String role;
}
