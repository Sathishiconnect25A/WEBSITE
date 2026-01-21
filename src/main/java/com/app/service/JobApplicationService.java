
package com.app.service;

import java.io.File;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.JobApplicationDTO;
import com.app.entity.JobApplication;
import com.app.repository.JobApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository repo;
    private final EmailService emailService;   // ✅ unified email service

    @Value("${file.upload-dir}")
    private String uploadDir;

    // ⭐ SAVE JOB APPLICATION
    public JobApplication saveApplication(JobApplicationDTO dto) throws Exception {

        MultipartFile resume = dto.getResume();

        // ✅ Validate resume
        if (resume == null || resume.isEmpty()) {
            throw new RuntimeException("Resume file is required");
        }

        // Create upload directory
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        // Clean filename
        String cleanedName = resume.getOriginalFilename()
                .replaceAll("[^a-zA-Z0-9._-]", "_")
                .toLowerCase();

        // Unique filename
        String fileName = UUID.randomUUID() + "_" + cleanedName;

        // Full file path
        Path path = Paths.get(uploadDir, fileName);

        // Save file
        Files.copy(resume.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Save DB
        JobApplication job = JobApplication.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .role(dto.getRole())
                .experienceType(dto.getExperienceType())
                .experienceYears(dto.getExperienceYears())
                .resumePath(path.toString())
                .build();

        JobApplication savedJob = repo.save(job);

        // ==================================================
        // 📩 EMAIL TRIGGER (JOB)
        // ==================================================
        try {
            emailService.sendEmail(
                    "JOB",                                 // ✅ application type
                    dto.getFullName(),
                    dto.getPhoneNumber(),
                    dto.getEmail(),
                    dto.getExperienceType(),               // qualification
                    String.valueOf(dto.getExperienceYears()),
                    dto.getRole(),                         // job role
                    "New job application received via portal",
                    path.toFile()                          // resume attachment
            );
        } catch (Exception e) {
            // Email should NOT break job application
            System.err.println("Email failed: " + e.getMessage());
        }

        return savedJob;
    }

    public JobApplication getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application Not Found: " + id));
    }

    public List<JobApplication> getAll() {
        return repo.findAll();
    }

    public boolean deleteById(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
