package com.application.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.demo.entity.Experience;

// ExperienceRepository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByEmail(String email);
}
