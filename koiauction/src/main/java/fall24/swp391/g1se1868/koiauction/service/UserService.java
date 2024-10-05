package fall24.swp391.g1se1868.koiauction.service;

import fall24.swp391.g1se1868.koiauction.model.LoginResponse;
import fall24.swp391.g1se1868.koiauction.model.User;
import fall24.swp391.g1se1868.koiauction.model.UserLogin;
import fall24.swp391.g1se1868.koiauction.model.UserPrinciple;
import fall24.swp391.g1se1868.koiauction.model.LoginResponse;
import fall24.swp391.g1se1868.koiauction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtService jwtService;

    private BCryptPasswordEncoder encoder =new BCryptPasswordEncoder(12);

    public String register(User user) {
        if (user == null) {
            return "User object cannot be null";
        }

        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            return "Username cannot be null or empty";
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Password cannot be null or empty";
        }

        if (verifyUserName(user.getUserName())) {
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRole("User");
            user.setStatus("Active");

            if (userRepository.save(user) != null) {
                return "Registered successfully";
            } else {
                return "Registration failed";
            }
        } else {
            return "Username already in use";
        }
    }
    public boolean verifyUserName(String username){
        User user = userRepository.findByUserName(username);
        return user==null?true:false;
    }
    public boolean verifyEmail(String email){
        User user = userRepository.findByEmail(email);
        return user==null?true:false;
    }
    public boolean verifyPhoneNumber(String phoneNumber){
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user==null?true:false;
    }
    public int getUserId(String username){
        User user = userRepository.findByUserName(username);
        return user==null?-1:user.getId();
    }
    public User getUserByUserName(String username){
        User user = userRepository.findByUserName(username);
        return user==null?null:user;
    }

    public ResponseEntity<?> login(UserLogin userLogin) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getUserName(), userLogin.getPassword())
            );

            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            User user = userPrinciple.getUser();

            if ("banned".equalsIgnoreCase(user.getStatus())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is banned and cannot log in.");
            }

            String token = jwtService.generateToken(user.getUserName(), user.getId());

            LoginResponse response = new LoginResponse(token, user.getUserName(), user.getFullName());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password is incorrect.");
        }
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public User createUser(User user) {
        user.setCreateAt(new Date().toInstant()); // Gán thời gian hiện tại
        user.setUpdateAt(new Date().toInstant());
        return userRepository.save(user);
    }


    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setRole(updatedUser.getRole());
            existingUser.setUserName(updatedUser.getUserName());
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setUpdateAt(new Date().toInstant()); // Gán lại thời gian cập nhật
            existingUser.setStatus(updatedUser.getStatus());
            return userRepository.save(existingUser);
        }
        return null;
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }



}

