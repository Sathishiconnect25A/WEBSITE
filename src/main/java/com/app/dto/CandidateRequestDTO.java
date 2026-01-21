package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateRequestDTO {

    private String candidateName;
    private String mobileNumber;
    private String email;

    private String qualification;
    private String yearOfPassing;
    private String courseInterested;

    private String keySkills;
    private String additionalDetails;
}
