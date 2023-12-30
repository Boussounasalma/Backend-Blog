package com.example.user.controller;

import com.example.user.entities.User;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ueser")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return Optional.ofNullable(updatedUser)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        User au = userService.authenticateSuperuser(email, password);
        if (au != null) {
            return new ResponseEntity<>(au, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/by-email")
    public User getSuperuserByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

    @PostMapping("/sendemail")
    public void sendemail(@RequestParam(name = "email") String email , @RequestParam(name = "id")int id ) {

        userService.sendemail(email,id);
    };


    @PutMapping("/updatePassword/{userId}")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        String password = request.get("Password");
        User updatedUser = userService.updatePassword(userId, password);

        if (updatedUser != null) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.notFound().build(); // User not found
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updatePassword(@PathVariable Long userId, @RequestBody User updatedUser) {
        User updatedUserData = userService.updateUser(userId, updatedUser);
        if (updatedUserData != null) {
            return new ResponseEntity<>(updatedUserData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
