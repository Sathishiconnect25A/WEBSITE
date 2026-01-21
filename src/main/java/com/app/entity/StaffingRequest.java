package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staffing_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String companyName;
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String staffingNeeds;
}
