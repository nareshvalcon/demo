package com.application.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.demo.entity.Education;
import com.application.demo.entity.Experience;
import com.application.demo.repository.ExperienceRepository;

// ExperienceService
@Service
public class ExperienceService {
    
    private final ExperienceRepository experienceRepository;

    @Autowired
    private AzureStorageService azureStorageService;

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

    // public List<Experience> getExperienceByEmail(String email) {
    //     return experienceRepository.findByEmail(email);
    // }

    public List<Experience> getExperienceByUserId(Long id) {
        return experienceRepository.findByUserId(id);
    }

    public String uploadImage(Long id, MultipartFile imageFile) {
        String blobName = generateBlobName(id, imageFile.getOriginalFilename());
        String imageUrl;
        try (InputStream data = imageFile.getInputStream()) {
            imageUrl = azureStorageService.upload(blobName, data, imageFile.getSize());
            Optional<Experience> optionalExperience = experienceRepository.findById(id);
            if (optionalExperience.isPresent()) {
                Experience experience = optionalExperience.get();
                experience.setImage(imageUrl);
                experienceRepository.save(experience);
            } else {
                // Experience with the provided ID does not exist
                throw new IOException("Experience with provided Id does not exist");
            }
        }
        catch(IOException ioe){
            //log message
            return ioe.getMessage();
        }
        catch(Exception ex){
            return ex.getMessage();
        }
        return imageUrl;
    }

    private String generateBlobName(Long id, String originalFilename) {
        // Generate a unique blob name using the email and original filename
        return id + "/" + UUID.randomUUID() + "_" + originalFilename;
    }
}

