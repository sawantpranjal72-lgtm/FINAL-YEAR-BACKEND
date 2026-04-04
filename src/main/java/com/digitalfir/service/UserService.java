package com.digitalfir.service;

import com.digitalfir.backend.dto.RegisterRequest;
import com.digitalfir.backend.model.Role;
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= REGISTER USER =================
    public User registerUser(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole())); // default role

        return userRepository.save(user);
    }

    // ================= GET USER BY EMAIL =================
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found with email: " + email)
                );}
// ================= ENABLE / DISABLE USER =================
    public void toggleUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }
 // ================= FIND USER BY RESET TOKEN =================
    public User findByResetToken(String token) {

        return userRepository.findByResetToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid reset token")
                );
    }


    // ================= SAVE USER =================
    public void save(User user){
        userRepository.save(user);
    }


    // ================= UPDATE PASSWORD =================
    public void updatePassword(User user,String newPassword){

        user.setPassword(passwordEncoder.encode(newPassword));

        user.setResetToken(null);

        userRepository.save(user);
    }
}

