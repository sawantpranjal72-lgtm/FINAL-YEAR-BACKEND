package com.digitalfir.controller;

import com.digitalfir.backend.model.PoliceProfile;
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.UserRepository;
import com.digitalfir.service.PoliceProfileService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/police/profile")
@CrossOrigin
public class PoliceProfileController {

    private final PoliceProfileService profileService;
    private final UserRepository userRepository;

    public PoliceProfileController(PoliceProfileService profileService,
                                   UserRepository userRepository) {
        this.profileService = profileService;
        this.userRepository = userRepository;
    }

    // ================= CREATE / UPDATE PROFILE =================
    // POLICE ONLY
    @PostMapping
    public ResponseEntity<?> createOrUpdateProfile(
            @RequestBody PoliceProfile profile,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PoliceProfile saved =
                profileService.createOrUpdate(profile, user);

        return ResponseEntity.ok(saved);
    }

    // ================= MY PROFILE =================
    // POLICE + ADMIN
    @GetMapping("/me")
    public ResponseEntity<?> myProfile(Authentication authentication) {

        String email = authentication.getName();
        Optional<PoliceProfile> profile =
                profileService.getByUserEmail(email);

        return profile
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 // ================= ADMIN – ALL POLICE PROFILES =================
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }
    
 // ================= UPLOAD PROFILE PHOTO =================
 // POLICE ONLY//
    @PostMapping(
    	    value = "/upload-photo",
    	    consumes = "multipart/form-data"
    	)
    	@PreAuthorize("hasRole('POLICE')")
    	public ResponseEntity<?> uploadProfilePhoto(
    	        @RequestParam("file") MultipartFile file,
    	        Authentication authentication) 
 {

     try {
         String email = authentication.getName();
         String fileName = profileService.uploadProfilePhoto(file, email);
         return ResponseEntity.ok(fileName);
     } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
     }
 }}





