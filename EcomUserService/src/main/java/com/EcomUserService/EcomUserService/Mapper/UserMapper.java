package com.EcomUserService.EcomUserService.Mapper;

import com.EcomUserService.EcomUserService.dto.UserDTO;
import com.EcomUserService.EcomUserService.model.User;

public class UserMapper {

    public static UserDTO toDTO(User user){
        return new UserDTO(user.getEmail(), user.getRoles());
    }
}
