package com.EcomUserService.EcomUserService.controller;

import com.EcomUserService.EcomUserService.dto.SetUserRoleRequestDTO;
import com.EcomUserService.EcomUserService.dto.UserDTO;
import com.EcomUserService.EcomUserService.exception.RoleNotFoundException;
import com.EcomUserService.EcomUserService.exception.UserNotFoundException;
import com.EcomUserService.EcomUserService.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable("id") Long userId) throws UserNotFoundException {
        UserDTO userDto = userService.getUserDetails(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDTO> setUserRoles(@PathVariable("id") Long userId,@RequestBody SetUserRoleRequestDTO request) throws UserNotFoundException, RoleNotFoundException {
        UserDTO userDto = userService.setUserRoles(userId,request.getRoleIds());
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
}
