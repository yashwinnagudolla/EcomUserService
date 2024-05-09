package com.EcomUserService.EcomUserService.service;

import com.EcomUserService.EcomUserService.Mapper.UserMapper;
import com.EcomUserService.EcomUserService.dto.UserDTO;
import com.EcomUserService.EcomUserService.exception.InvalidCredentialException;
import com.EcomUserService.EcomUserService.exception.InvalidTokenException;
import com.EcomUserService.EcomUserService.exception.UserNotFoundException;
import com.EcomUserService.EcomUserService.model.Session;
import com.EcomUserService.EcomUserService.model.SessionStatus;
import com.EcomUserService.EcomUserService.model.User;
import com.EcomUserService.EcomUserService.repository.SessionRepository;
import com.EcomUserService.EcomUserService.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {
    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecretKey key;

    private MacAlgorithm algo = Jwts.SIG.HS256;

    public AuthService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public ResponseEntity<List<Session>> getAllSessions(){
        List<Session> sessions = sessionRepository.findAll();
        return ResponseEntity.ok(sessions);
    }

    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<UserDTO> login(String email,String password) throws UserNotFoundException, InvalidCredentialException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("The user with email "+email+" does not exist");
        }
        User user = userOptional.get();

        //checking for password mismatch
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new InvalidCredentialException("The password is incorrect");
        }
        String token = generateToken(user);

        //creating new session
        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        session.setLoginAt(new Date());
        session.setExpitingAt(new Date(LocalDate.now().plusDays(3).toEpochDay()));
        session.setStatus(SessionStatus.ACTIVE);
        sessionRepository.save(session);
        UserDTO userDTO = UserMapper.toDTO(user);

        //Setting up the headers
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE,token);
        return new ResponseEntity<>(userDTO,headers, HttpStatus.OK);

    }

    public ResponseEntity<Void> logOut(String token, Long userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("The user with id "+userId+" does not exist");
        }
        User user = userOptional.get();
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUserId(token,userId);
        if(sessionOptional.isEmpty()){
            throw new UserNotFoundException("The user with id "+userId+" does not have an active session");
        }
        Session session = sessionOptional.get();
        session.setStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();

    }

    public UserDTO signup(String email, String password){
        User user = new User();
        user.setEmail(email);
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    public SessionStatus validate(String token,Long userId) throws InvalidTokenException {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUserId(token,userId);
        if(sessionOptional.isEmpty() || sessionOptional.get().getStatus().equals(SessionStatus.ENDED)){
            throw new InvalidTokenException("The token is invalid");
        }
        Jws<Claims> claimsJWS = Jwts.parser().setSigningKey(key).build().parseSignedClaims(token);
        Long tokenUserId = Long.parseLong(claimsJWS.getBody().get("userId").toString());
        if(!tokenUserId.equals(userId)){
            throw new InvalidTokenException("The token is invalid");
        }

        Date expirateAt = claimsJWS.getBody().get("expiringAt", Date.class);
        if(expirateAt.before(new Date())){
            throw new InvalidTokenException("The token is expired on" + expirateAt);
        }
        return sessionOptional.get().getStatus();

    }

    public void generateKey(){
        SecretKey key = algo.key().build();
        this.key = key;
    }

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("roles" , user.getRoles());
        claims.put("createdAt", new Date());
        claims.put("expiringAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));

        String token = Jwts.builder().claims(claims).signWith(key,algo).compact();
        return token;
    }
}
