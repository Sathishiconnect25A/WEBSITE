package com.app.service;



import java.util.List;

import com.app.dto.StudentApplicationDTO;
import com.app.entity.StudentApplication;

public interface StudentApplicationService {

	StudentApplication submitApplication(StudentApplicationDTO dto);

    List<StudentApplication> getAllApplications();

    StudentApplication getApplicationById(Long id);
}
