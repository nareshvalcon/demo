package com.application.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.demo.entity.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByEmail(String email);
}

