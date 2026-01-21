package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String candidateName;
    private String mobileNumber;
    private String emailAddress;

    // 👉 No More Enums
    private String qualification;
    private String yearOfPassing;
    private String courseInterested;

    private String keySkills;

    @Column(length = 1500)
    private String additionalDetails;

    private String resumePath;
}
