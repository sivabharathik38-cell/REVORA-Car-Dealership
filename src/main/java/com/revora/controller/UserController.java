package com.revora.controller;

import com.revora.model.User;
import com.revora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Simple plain-text password match for easy deployment/development
            if (user.getPassword().equals(loginRequest.getPassword())) {
                // Clear password in response for safety
                User responseUser = new User(user.getUsername(), "", user.getEmail(), user.getPhone(), user.getAddress(), user.getRole());
                responseUser.setId(user.getId());
                return ResponseEntity.ok(responseUser);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    // Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        User savedUser = userRepository.save(user);
        savedUser.setPassword(""); // Clear password in response
        return ResponseEntity.ok(savedUser);
    }

    // Get User Profile
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(""); // Clear password
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // Update User Profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody User profileUpdate) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmail(profileUpdate.getEmail());
            user.setPhone(profileUpdate.getPhone());
            user.setAddress(profileUpdate.getAddress());
            if (profileUpdate.getUsername() != null && !profileUpdate.getUsername().isEmpty()) {
                user.setUsername(profileUpdate.getUsername());
            }
            User updatedUser = userRepository.save(user);
            updatedUser.setPassword(""); // Clear password
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    // List Users (Admin view)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword("")); // Clear passwords
        return ResponseEntity.ok(users);
    }

    // Delete User (Admin action)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
