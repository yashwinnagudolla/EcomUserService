package com.EcomUserService.EcomUserService.controller;

import com.EcomUserService.EcomUserService.dto.CreateRoleRequestDTO;
import com.EcomUserService.EcomUserService.model.Role;
import com.EcomUserService.EcomUserService.service.RoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public void createRole(@RequestBody CreateRoleRequestDTO request){
        Role role = roleService.createRole(request.getName());
    }

}
