package com.digitalfir.service;

import com.digitalfir.backend.model.FIR;
import com.digitalfir.backend.model.FirStatus;
import com.digitalfir.backend.model.Role;
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.FIRRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class FIRService {

    @Autowired
    private FIRRepository firRepository;

    @Autowired
    private UserService userService;

    // ================= CREATE FIR =================
    public FIR createFIR(FIR fir, String userEmail) {

        User user = userService.getUserByEmail(userEmail);

        if (user.getRole() != Role.CITIZEN) {
            throw new RuntimeException("Only Citizen can create FIR");
        }

        fir.setCreatedBy(user.getId());
        fir.setStatus(FirStatus.SUBMITTED);

        // STEP 1: save to get ID
        FIR savedFir = firRepository.save(fir);

        // STEP 2: generate FIR Number
        String firNumber = "FIR-" + Year.now().getValue() + "-" +
                String.format("%04d", savedFir.getId());

        savedFir.setFirNumber(firNumber);

        return firRepository.save(savedFir);
    }

    // ================= GET MY FIR =================
    public List<FIR> getFirsByUser(String userEmail) {

        User user = userService.getUserByEmail(userEmail);

        if (user.getRole() != Role.CITIZEN) {
            throw new RuntimeException("Only Citizen can view their FIRs");
        }

        return firRepository.findByCreatedBy(user.getId());
    }

    // ================= GET ALL FIR =================
    public List<FIR> getAllFirs(String userEmail) {

        User user = userService.getUserByEmail(userEmail);

        if (user.getRole() == Role.CITIZEN) {
            throw new RuntimeException("Citizen cannot view all FIRs");
        }

        return firRepository.findAll();
    }

    // ================= TRACK BY FIR NUMBER 🔥 =================
    public FIR getByFirNumber(String firNumber) {

        return firRepository.findByFirNumber(firNumber)
                .orElseThrow(() -> new RuntimeException("FIR not found"));
    }

    // ================= UPDATE STATUS =================
    public FIR updateFIRStatus(Long firId, FirStatus newStatus, String userEmail) {

        User user = userService.getUserByEmail(userEmail);

        FIR fir = firRepository.findById(firId)
                .orElseThrow(() -> new RuntimeException("FIR not found"));

        if (user.getRole() == Role.CITIZEN) {
            throw new RuntimeException("Citizen cannot update FIR");
        }

        fir.setStatus(newStatus);
        return firRepository.save(fir);
    }

    // ================= DELETE =================
    public void deleteFIR(Long firId, String userEmail) {

        User user = userService.getUserByEmail(userEmail);

        if (user.getRole() == Role.CITIZEN) {
            throw new RuntimeException("Citizen cannot delete FIR");
        }

        FIR fir = firRepository.findById(firId)
                .orElseThrow(() -> new RuntimeException("FIR not found"));

        firRepository.delete(fir);
    }
}