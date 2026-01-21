package com.app.controller;



import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


import java.io.File;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.CandidateRequestDTO;
import com.app.entity.CandidateApplication;
import com.app.service.CandidateService;

@RestController
@RequestMapping("/api/candidate")
@CrossOrigin("*")
public class CandidateController {

    private final CandidateService service;

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitCandidate(
            @ModelAttribute CandidateRequestDTO dto,
            @RequestParam("resume") MultipartFile resumeFile
    ) {
        try {
            CandidateApplication saved = service.saveCandidate(dto, resumeFile);
            return ResponseEntity.ok("Application Submitted Successfully. ID: " + saved.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/allCandidates")
    public ResponseEntity<?> getAllCandidates() {
        return ResponseEntity.ok(service.getAllCandidates());
    }
    
    @GetMapping("/candidateById")
    public ResponseEntity<?> getCandidateById(@RequestParam Long id) {
        return ResponseEntity.ok(service.getCandidateById(id));
    }
    
    
    @GetMapping("/resume/candidateId")
    public ResponseEntity<Resource> downloadResume(@RequestParam Long id) throws Exception {

        CandidateApplication candidate = service.getCandidateById(id);

        File file = new File(candidate.getResumePath());
        if (!file.exists()) {
            throw new RuntimeException("Resume not found");
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
    @GetMapping("/dropdowns")
    public ResponseEntity<?> getDropdownData() {

        return ResponseEntity.ok(
            java.util.Map.of(
                "qualification", java.util.List.of(
                    "BTech",
                    "MTech",
                    "MCA",
                    "Diploma",
                    "Intermediate",
                    "BSc",
                    "BCom",
                    "BA",
                    "BBA",
                    "BCA"
                ),
                "yearOfPassing", java.util.List.of(
                    "2020", "2021", "2022", "2023",
                    "2024", "2025", "2026"
                ),
                "courseInterested", java.util.List.of(
                    "Fullstack Java",
                    "Fullstack Python",
                    "Cyber Security",
                    "React Native",
                    "React JS",
                    "Testing",
                    "Data Analyst",
                    "SAP",
                    "Dot Net",
                    "MERN Stack",
                    "AI & ML"
                )
            )
        );
    }


}
