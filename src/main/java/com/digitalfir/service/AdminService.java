package com.digitalfir.service;

import com.digitalfir.backend.dto.AddPoliceRequest;
import com.digitalfir.backend.model.NotificationType;
import com.digitalfir.backend.model.Role;
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private NotificationService notificationService;

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AdminService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    // ================= USERS =================
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // ================= ADD POLICE =================
    public void addPolice(AddPoliceRequest req) {

        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new RuntimeException("Password cannot be empty");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.POLICE);
        user.setEnabled(true);

        userRepo.save(user);
    }

    // ================= ENABLE / DISABLE =================
    public void toggleUser(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(!user.isEnabled());
        userRepo.save(user);

        String msg = user.isEnabled()
                ? "Your account has been ENABLED by admin"
                : "Your account has been DISABLED by admin";

        notificationService.createNotification(
                user,
                msg,
                NotificationType.ACCOUNT_STATUS,
                user.getId()
        );
    }

    // ================= DASHBOARD STATS =================
    public Map<String, Long> getDashboardStats() {

        Map<String, Long> map = new HashMap<>();

        map.put("totalUsers", userRepo.count());
        map.put("policeCount", userRepo.countByRole(Role.POLICE));
        map.put("citizenCount", userRepo.countByRole(Role.CITIZEN));
        map.put("disabledUsers", userRepo.countByEnabledFalse());

        return map;
    }
}
