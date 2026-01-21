package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String company;
    private String phone;
    private String service;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;
}
