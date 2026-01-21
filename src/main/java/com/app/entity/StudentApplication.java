package com.app.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentApplication {

	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String contactPersonName;

	    private String mobileNumber;

	    private String emailAddress;

	    private String schoolName;

	    private String schoolAddress;

	    private Integer approxNoOfStudents;

	    private String currentSoftwareUsed;

	    @Column(length = 1000)
	    private String trainingNeeds;
	

}
