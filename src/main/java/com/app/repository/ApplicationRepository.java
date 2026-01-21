package com.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByEmail(String email);

    boolean existsByEmail(String email);
}
