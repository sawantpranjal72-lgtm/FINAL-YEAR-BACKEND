package com.digitalfir.controller;

import com.digitalfir.backend.dto.AddPoliceRequest;
import com.digitalfir.backend.dto.PoliceProfileResponse;
import com.digitalfir.backend.model.PoliceProfile;
import com.digitalfir.backend.model.User;
import com.digitalfir.service.AdminService;
import com.digitalfir.service.PoliceProfileService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin
public class AdminController {

    private final AdminService adminService;
    private final PoliceProfileService policeProfileService;

    public AdminController(AdminService adminService,
                           PoliceProfileService policeProfileService) {
        this.adminService = adminService;
        this.policeProfileService = policeProfileService;
    }

    // ================= USERS =================
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // ================= ADD POLICE =================
    @PostMapping("/add-police")
    public ResponseEntity<?> addPolice(
            @RequestBody AddPoliceRequest request) {

        adminService.addPolice(request);
        return ResponseEntity.ok("Police added successfully");
    }

    // ================= ENABLE / DISABLE =================
    @PutMapping("/toggle-user/{id}")
    public ResponseEntity<?> toggleUser(@PathVariable Long id) {
        adminService.toggleUser(id);
        return ResponseEntity.ok("User status updated");
    }

    // ================= STATS =================
    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return adminService.getDashboardStats();
    }

    // ================= POLICE PROFILES =================
 // ================= GET ALL POLICE PROFILES =================
    @GetMapping("/police-profiles")
    public List<PoliceProfileResponse> getAllPoliceProfiles() {

        List<PoliceProfile> profiles =
                policeProfileService.getAllProfiles();

        return profiles.stream().map(p -> {

            PoliceProfileResponse dto =
                    new PoliceProfileResponse();

            dto.setId(p.getId());
            dto.setFullName(p.getFullName());
            dto.setBadgeNumber(p.getBadgeNumber());
            dto.setPoliceStation(p.getPoliceStation());
            dto.setRank(p.getRank());
            dto.setProfilePhoto(p.getProfilePhoto());

            // 🔥 THIS IS THE FIX
            if (p.getUser() != null) {
                dto.setEmail(p.getUser().getEmail());

                // 🔥 ADD
                dto.setEnabled(p.getUser().isEnabled());
                dto.setUserId(p.getUser().getId());
            }

            return dto;

        }).collect(Collectors.toList());
    }

   

}






    


