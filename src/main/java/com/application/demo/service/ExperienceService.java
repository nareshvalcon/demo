package com.application.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.demo.entity.Experience;
import com.application.demo.repository.ExperienceRepository;

// ExperienceService
@Service
public class ExperienceService {
    
    private final ExperienceRepository experienceRepository;

    @Autowired
    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public Experience saveExperience(Experience experience){
        return experienceRepository.save(experience);
    }

    public List<Experience> getExperience() {
        return experienceRepository.findAll();
    }

    public Optional<Experience> getExperienceById(Long id) {
        return experienceRepository.findById(id);
    }

    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    public List<Experience> getExperienceByEmail(String email) {
        return experienceRepository.findByEmail(email);
    }
}

