package com.EcomUserService.EcomUserService.service;

import com.EcomUserService.EcomUserService.Mapper.UserMapper;
import com.EcomUserService.EcomUserService.dto.UserDTO;
import com.EcomUserService.EcomUserService.exception.RoleNotFoundException;
import com.EcomUserService.EcomUserService.exception.UserNotFoundException;
import com.EcomUserService.EcomUserService.model.Role;
import com.EcomUserService.EcomUserService.model.User;
import com.EcomUserService.EcomUserService.repository.RoleRepository;
import com.EcomUserService.EcomUserService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository,RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDTO createUser(String email, List<Long> roleIds) throws RoleNotFoundException {
        User user = new User();
        user.setEmail(email);
        Optional<Set<Role>> rolesOptional = roleRepository.findAllByIdIn(roleIds);
        if(rolesOptional.isEmpty()){
            throw new RoleNotFoundException("The role with id "+roleIds+" does not exist");
        }
        user.setRoles(rolesOptional.get());
        userRepository.save(user);
        return UserMapper.toDTO(user);

    }

    public UserDTO getUserDetails(Long userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("The user with id "+userId+" does not exist");

        }
        UserDTO userDto = new UserDTO();
        return UserMapper.toDTO(userOptional.get());
    }

    public UserDTO setUserRoles(Long userId, List<Long> roleIds) throws UserNotFoundException, RoleNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("The user with id "+userId+" does not exist");
        }
        Optional<Set<Role>> roles = roleRepository.findAllByIdIn(roleIds);
        User user = userOptional.get();
        if(roles.isEmpty()){
            throw new RoleNotFoundException("The role with id "+roleIds+" does not exist");
        }
        user.setRoles(roles.get());
        userRepository.save(user);
        return UserMapper.toDTO(user);
    }
}
