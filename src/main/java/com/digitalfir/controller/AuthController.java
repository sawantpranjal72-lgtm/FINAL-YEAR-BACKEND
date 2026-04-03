package com.digitalfir.controller;

import com.digitalfir.backend.dto.LoginRequest;
import com.digitalfir.backend.dto.RegisterRequest;
import com.digitalfir.backend.model.User;
import com.digitalfir.service.JwtService;
import com.digitalfir.service.UserService;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }


    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "role", userDetails.getAuthorities()
                                               .iterator()
                                               .next()
                                               .getAuthority()
                    )
            );

        } catch (AuthenticationException e) {

            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Invalid email or password"));

        } catch (Exception e) {

            e.printStackTrace();  

            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "Something went wrong"));
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String,String> request){

        String email = request.get("email");

        User user = userService.getUserByEmail(email);

        String token = java.util.UUID.randomUUID().toString();

        user.setResetToken(token);

        userService.save(user);

        return ResponseEntity.ok(Map.of(
                "message","Reset token generated",
                "token",token
        ));
    }


    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> request){

        String token = request.get("token");
        String newPassword = request.get("password");

        User user = userService.findByResetToken(token);

        userService.updatePassword(user,newPassword);

        return ResponseEntity.ok("Password updated successfully");
    }
}