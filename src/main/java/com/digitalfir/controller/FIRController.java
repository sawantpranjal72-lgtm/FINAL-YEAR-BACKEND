package com.digitalfir.controller;

import com.digitalfir.backend.model.FIR;
import com.digitalfir.backend.model.FirStatus;
import com.digitalfir.backend.model.FirStatusHistory;
import com.digitalfir.service.FIRService;
import com.digitalfir.repository.FirStatusHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fir")
@CrossOrigin(origins = "*")
public class FIRController {

    @Autowired
    private FIRService firService;

    @Autowired
    private FirStatusHistoryRepository historyRepository;

    // ================= CREATE FIR =================
    @PostMapping("/create")
    public ResponseEntity<FIR> createFIR(@RequestBody FIR fir, Authentication auth) {

        return ResponseEntity.ok(
                firService.createFIR(fir, auth.getName())
        );
    }

    // ================= TRACK FIR 🔥 =================
    @GetMapping("/track/{firNumber}")
    public ResponseEntity<FIR> trackFIR(@PathVariable String firNumber) {

        return ResponseEntity.ok(
                firService.getByFirNumber(firNumber)
        );
    }

    // ================= GET ALL =================
    @GetMapping("/all")
    public ResponseEntity<List<FIR>> getAllFirs(Authentication auth) {

        return ResponseEntity.ok(
                firService.getAllFirs(auth.getName())
        );
    }

    // ================= GET MY FIR =================
    @GetMapping("/my")
    public ResponseEntity<List<FIR>> getMyFirs(Authentication auth) {

        return ResponseEntity.ok(
                firService.getFirsByUser(auth.getName())
        );
    }

    // ================= UPDATE STATUS =================
    @PutMapping("/update-status/{id}")
    public ResponseEntity<FIR> updateStatus(
            @PathVariable Long id,
            @RequestParam FirStatus status,
            Authentication auth) {

        return ResponseEntity.ok(
                firService.updateFIRStatus(id, status, auth.getName())
        );
    }

    // ================= HISTORY =================
    @GetMapping("/{id}/history")
    public ResponseEntity<List<FirStatusHistory>> getStatusHistory(@PathVariable Long id) {

        return ResponseEntity.ok(
                historyRepository.findByFirId(id)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFIR(@PathVariable Long id, Authentication auth) {

        firService.deleteFIR(id, auth.getName());

        return ResponseEntity.ok("FIR deleted successfully");
    }
}