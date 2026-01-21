package com.app.entity;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerItem {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Enumerated(EnumType.STRING)
    private MediaSection section;
 
 // ✅ NEW FIELD (ADD ONLY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PageType pageType;
 
    // Instead of list of files, we keep two fields only
    private String imageUrl;
    private String videoUrl;
}

 
 