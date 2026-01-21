package com.app.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationDTO {

    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private String experienceType;   // Fresher / Experienced
    private Integer experienceYears; // if experienced

    private MultipartFile resume;    // File upload
}
