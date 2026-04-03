package com.digitalfir.controller;

import com.digitalfir.backend.model.Evidence;
import com.digitalfir.service.EvidenceService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/evidence")
@CrossOrigin("*")
public class EvidenceController {

    @Autowired
    private EvidenceService evidenceService;

    // ================= UPLOAD =================
    @PostMapping(value = "/upload/{firId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Evidence> upload(
            @PathVariable Long firId,
            @RequestPart MultipartFile file) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(
                evidenceService.uploadEvidence(firId, file, auth.getName()));
    }

    // ================= GET BY FIR =================
    @GetMapping("/fir/{firId}")
    public ResponseEntity<List<Evidence>> getByFir(@PathVariable Long firId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(
                evidenceService.getEvidenceByFir(firId, auth.getName()));
    }

    // ================= PUBLIC VIEW =================
    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> view(@PathVariable Long id) throws IOException {

        Resource file = evidenceService.viewEvidencePublic(id);
        String type = Files.probeContentType(file.getFile().toPath());

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(type))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }


    // ================= ADMIN VIEW =================
    @GetMapping("/admin/view/{id}")
    @PreAuthorize("hasAnyRole('POLICE','ADMIN')")
    public ResponseEntity<Resource> adminView(@PathVariable Long id) {

        Resource file = evidenceService.viewEvidenceForAdmin(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('POLICE','ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        evidenceService.deleteEvidence(id, auth.getName());

        return ResponseEntity.ok("Evidence deleted successfully");
    }
}



