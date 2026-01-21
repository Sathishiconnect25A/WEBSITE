package com.app.controller;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.dto.JobApplicationDTO;
import com.app.entity.JobApplication;
import com.app.service.JobApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin("*")
public class JobApplicationController {

    private final JobApplicationService service;

    // ⭐ APPLY JOB
    @PostMapping("/apply")
    public ResponseEntity<?> apply(@ModelAttribute JobApplicationDTO dto) {
        try {

            if (dto.getResume() == null || dto.getResume().isEmpty()) {
                return ResponseEntity.badRequest().body("Resume file is mandatory");
            }

            service.saveApplication(dto);
            return ResponseEntity.ok("Application Submitted Successfully!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }

    // ⭐ GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ⭐ GET ALL
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ⭐ DELETE BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return service.deleteById(id)
                ? ResponseEntity.ok("Deleted successfully")
                : ResponseEntity.notFound().build();
    }

    // ⭐ DOWNLOAD RESUME
    @GetMapping("/resume")
    public ResponseEntity<Resource> downloadResume(@RequestParam Long id) {

        JobApplication app = service.getById(id);
        File file = new File(app.getResumePath());

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
}
