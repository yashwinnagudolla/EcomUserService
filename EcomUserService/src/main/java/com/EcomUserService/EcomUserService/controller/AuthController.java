package com.EcomUserService.EcomUserService.controller;

import com.EcomUserService.EcomUserService.dto.*;
import com.EcomUserService.EcomUserService.exception.InvalidCredentialException;
import com.EcomUserService.EcomUserService.exception.InvalidTokenException;
import com.EcomUserService.EcomUserService.exception.UserNotFoundException;
import com.EcomUserService.EcomUserService.model.SessionStatus;
import com.EcomUserService.EcomUserService.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO request){
        UserDTO userDto = authService.signup(request.getEmail(),request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO request) throws UserNotFoundException, InvalidCredentialException {
        return authService.login(request.getEmail(),request.getPassword());
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<Void> logout(@PathVariable("id") Long userId, @RequestHeader("token") String token) throws UserNotFoundException {
        return authService.logOut(token,userId);
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequestDTO request) throws InvalidTokenException {
        SessionStatus status = authService.validate(request.getToken(),request.getUserId());
        return new ResponseEntity<>(status,HttpStatus.OK);

    }


}
