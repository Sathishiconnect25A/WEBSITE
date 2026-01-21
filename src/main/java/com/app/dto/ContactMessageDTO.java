package com.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessageDTO {

    private String fullName;
    private String email;
    private String company;
    private String phone;
    private String service;
    private String subject;
    private String message;
}
