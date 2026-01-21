package com.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffingRequestDTO {

    private String fullName;
    private String email;
    private String companyName;
    private String phoneNumber;
    private String staffingNeeds;
}
