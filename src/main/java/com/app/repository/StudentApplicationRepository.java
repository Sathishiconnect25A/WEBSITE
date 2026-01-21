package com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.StudentApplication;

public interface StudentApplicationRepository
        extends JpaRepository<StudentApplication, Long> {
}
