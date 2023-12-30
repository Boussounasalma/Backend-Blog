package com.example.user.service;

import com.example.user.entities.User;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }


    public User updateUser(Long userId, User user) {
        if (userRepository.existsById(userId)) {
            user.setId(userId);
            return userRepository.save(user);
        } else {
            // Gérer l'absence de l'utilisateur avec l'ID spécifié
            return null;
        }
    }


    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User authenticateSuperuser(String email, String password) {
        // Retrieve the Superuser based on the provided username
        User superuser = userRepository.findByEmail(email);

        // Check if the Superuser exists and if the password matches
        if (superuser != null && superuser.getPassword().equals(password)) {
            return superuser; // Return the authenticated Superuser
        }

        return null; // Return null if authentication fails
    }
}
