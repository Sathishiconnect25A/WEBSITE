package com.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.CandidateRequestDTO;
import com.app.entity.CandidateApplication;
import com.app.repository.CandidateRepository;

@Service
public class CandidateService {

    private final CandidateRepository repository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public CandidateService(CandidateRepository repository) {
        this.repository = repository;
    }

    public CandidateApplication saveCandidate(CandidateRequestDTO dto, MultipartFile resumeFile) throws IOException {

        // Create directory if not exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Clean filename
        String fileName = UUID.randomUUID() + "_" + resumeFile.getOriginalFilename();

        // Final file path
        Path filePath = Paths.get(uploadDir, fileName);

        // Save file
        Files.copy(resumeFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        CandidateApplication candidate = CandidateApplication.builder()
                .candidateName(dto.getCandidateName())
                .mobileNumber(dto.getMobileNumber())
                .emailAddress(dto.getEmail())

                // 👉 Now using String
                .qualification(dto.getQualification())
                .yearOfPassing(dto.getYearOfPassing())
                .courseInterested(dto.getCourseInterested())

                .keySkills(dto.getKeySkills())
                .additionalDetails(dto.getAdditionalDetails())
                .resumePath(filePath.toString())
                .build();

        return repository.save(candidate);
    }

    public List<CandidateApplication> getAllCandidates() {
        return repository.findAll();
    }

    public CandidateApplication getCandidateById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
    }
}
