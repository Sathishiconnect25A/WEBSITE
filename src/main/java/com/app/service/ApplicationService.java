
package com.app.service;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Application;
import com.app.repository.ApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository repository;
    private final EmailService emailService;

    @Transactional
    public void submitApplication(
            String name,
            String mobile,
            String email,
            String qualification,
            String yearOfPassing,
            String course,
            String message,
            MultipartFile resume
    ) {

        if (repository.existsByEmail(email)) {
            throw new RuntimeException("Already registered with this email");
        }

        try {
            // ===============================
            // 📁 FILE SAVE
            // ===============================
            String uploadPath = "C:/uploads/";
            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            if (resume == null || resume.isEmpty()) {
                throw new RuntimeException("Resume file is required");
            }

            File resumeFile = new File(
                    uploadPath + System.currentTimeMillis()
                    + "_" + resume.getOriginalFilename()
            );
            resume.transferTo(resumeFile);

            // ===============================
            // 💾 SAVE DATABASE
            // ===============================
            Application app = new Application();
            app.setName(name);
            app.setMobile(mobile);
            app.setEmail(email);
            app.setQualification(qualification);
            app.setYearOfPassing(yearOfPassing);
            app.setCourse(course);
            app.setMessage(message);
            app.setResumePath(resumeFile.getAbsolutePath());

            repository.save(app);

            // ===============================
            // 📩 SEND EMAILS (NON-CRITICAL)
            // ===============================
            try {
                emailService.sendEmail(
                        "INTERNSHIP",           // ✅ APPLICATION TYPE
                        name,
                        mobile,
                        email,
                        qualification,
                        yearOfPassing,
                        course,
                        message,
                        resumeFile              // attachment
                );
            } catch (Exception e) {
                System.err.println("Email failed: " + e.getMessage());
            }

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Application submission failed: " + ex.getMessage()
            );
        }
    }
}
            		
