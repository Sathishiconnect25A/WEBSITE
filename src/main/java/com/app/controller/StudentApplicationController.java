package com.app.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.dto.StudentApplicationDTO;
import com.app.entity.StudentApplication;
import com.app.service.StudentApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentApplicationController {

    private final StudentApplicationService service;

    // ✅ POST API - Submit Form
    @PostMapping("/submit")
    public ResponseEntity<StudentApplication> submitApplication(
            @RequestBody StudentApplicationDTO dto) {

    	StudentApplication saved = service.submitApplication(dto);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET API - All Applications
    @GetMapping("/all")
    public ResponseEntity<List<StudentApplication>> getAllApplications() {
        return ResponseEntity.ok(service.getAllApplications());
    }

    // ✅ GET API - By ID
    @GetMapping("/getById")
    public ResponseEntity<StudentApplication> getById(@RequestParam Long id) {
        return ResponseEntity.ok(service.getApplicationById(id));
    }
}
