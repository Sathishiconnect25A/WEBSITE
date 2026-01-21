package com.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentApplicationDTO {

    private String contactPersonName;

    private String mobileNumber;

    private String emailAddress;

    private String schoolName;

    private String schoolAddress;

    private Integer approxNoOfStudents;

    private String currentSoftwareUsed;

    private String trainingNeeds;
}
