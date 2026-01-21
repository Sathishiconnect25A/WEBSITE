package com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.app.dto.StudentApplicationDTO;
import com.app.entity.StudentApplication;
import com.app.repository.StudentApplicationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public  class StudentApplicationServiceImpl implements StudentApplicationService {

    private final StudentApplicationRepository repository;

    @Override
    public StudentApplication submitApplication(StudentApplicationDTO dto) {

    	StudentApplication application = StudentApplication.builder()
                .contactPersonName(dto.getContactPersonName())
                .mobileNumber(dto.getMobileNumber())
                .emailAddress(dto.getEmailAddress())
                .schoolName(dto.getSchoolName())
                .schoolAddress(dto.getSchoolAddress())
                .approxNoOfStudents(dto.getApproxNoOfStudents())
                .currentSoftwareUsed(dto.getCurrentSoftwareUsed())
                .trainingNeeds(dto.getTrainingNeeds())
                .build();

        return repository.save(application);
    }

    @Override
    public List<StudentApplication> getAllApplications() {
        return repository.findAll();
    }

    @Override
    public StudentApplication getApplicationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));
    }
}
