package com.digitalfir.controller;

import com.digitalfir.backend.model.PoliceProfile;
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/police")
@CrossOrigin(origins = "*")
public class PoliceController {

    private final UserRepository userRepository;

    public PoliceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ================= POLICE PROFILE =================
    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Police not found"));
    }

}

