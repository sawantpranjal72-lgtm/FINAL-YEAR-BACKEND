package com.digitalfir.service;

import com.digitalfir.backend.model.PoliceProfile;
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.PoliceProfileRepository;
import com.digitalfir.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PoliceProfileService {

    private final PoliceProfileRepository profileRepository;
    private final UserRepository userRepository;

    // ✅ constructor injection (BEST PRACTICE)
    public PoliceProfileService(PoliceProfileRepository profileRepository,
                                UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    // ================= CREATE / UPDATE =================
    public PoliceProfile createOrUpdate(PoliceProfile profile, User user) {

        Optional<PoliceProfile> existing =
                profileRepository.findByUser(user);

        if (existing.isPresent()) {
            PoliceProfile p = existing.get();
            p.setFullName(profile.getFullName());
            p.setBadgeNumber(profile.getBadgeNumber());
            p.setPoliceStation(profile.getPoliceStation());
            p.setRank(profile.getRank());
           
            return profileRepository.save(p);
        }

       
        profile.setUser(user);
     // createdAt automatically set by @PrePersist
     return profileRepository.save(profile);

       
    }

    // ================= GET BY EMAIL =================
    public Optional<PoliceProfile> getByUserEmail(String email) {

        return userRepository.findByEmail(email)
                .flatMap(profileRepository::findByUser);
    }

    // ================= GET ALL (ADMIN) =================
    public List<PoliceProfile> getAllProfiles() {
        return profileRepository.findAll();
    }

    // ================= UPLOAD PHOTO =================
    public String uploadProfilePhoto(MultipartFile file, String email)
            throws Exception {

        // ---------- validation ----------
        if (file.isEmpty())
            throw new RuntimeException("File is empty");

        if (file.getSize() > 2 * 1024 * 1024)
            throw new RuntimeException("Max size 2MB");

        String type = file.getContentType();
        if (!"image/jpeg".equals(type) && !"image/png".equals(type)) {
            throw new RuntimeException("Only JPG or PNG allowed");
        }

        // ---------- fetch user ----------
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PoliceProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // ---------- image processing ----------
        BufferedImage original =
                ImageIO.read(file.getInputStream());

        BufferedImage cropped = cropCenterSquare(original);

        String dirPath = "uploads/profile/";
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        String fileName =
                System.currentTimeMillis() + "_profile.jpg";

        File output =
                new File(dirPath + fileName);

        ImageIO.write(cropped, "jpg", output);

        profile.setProfilePhoto(fileName);
        profileRepository.save(profile);

        return fileName;
    }

    // ================= IMAGE CROP =================
    private BufferedImage cropCenterSquare(BufferedImage src) {

        int w = src.getWidth();
        int h = src.getHeight();
        int size = Math.min(w, h);

        int x = (w - size) / 2;
        int y = (h - size) / 2;

        return src.getSubimage(x, y, size, size);
    }


    
}



