package com.app.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.service.ApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/apply")
@RequiredArgsConstructor
@CrossOrigin
public class ApplicationController {

    private final ApplicationService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> apply(
            @RequestParam String name,
            @RequestParam String mobile,
            @RequestParam String email,
            @RequestParam String qualification,
            @RequestParam String yearOfPassing,
            @RequestParam String course,
            @RequestParam(required = false) String message,
            @RequestParam MultipartFile resume
    ) throws Exception {

        service.submitApplication(
                name,
                mobile,
                email,
                qualification,
                yearOfPassing,
                course,
                message,
                resume
        );

        return ResponseEntity.ok("Application submitted successfully");
    }
}
