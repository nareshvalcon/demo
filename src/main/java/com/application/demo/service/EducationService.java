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
import com.application.demo.repository.EducationRepository;

@Service
public class EducationService {

    private final EducationRepository educationRepository;

    @Autowired
    private AzureStorageService azureStorageService;

    @Autowired
    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    public Education saveEducation(Education education) {
        return educationRepository.save(education);
    }

    public List<Education> getEducations() {
        return educationRepository.findAll();
    }

    public Optional<Education> getEducationById(Long id) {
        return educationRepository.findById(id);
    }

    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }

    // public List<Education> getEducationByEmail(String email) {
    //     return educationRepository.findByEmail(email);
    // }

    public List<Education> getEducationByUserId(Long id) {
        return educationRepository.findByUserId(id);
    }

    public String uploadImage(Long id, MultipartFile imageFile) {
        String blobName = generateBlobName(id, imageFile.getOriginalFilename());
        String imageUrl;
        try (InputStream data = imageFile.getInputStream()) {
            imageUrl = azureStorageService.upload(blobName, data, imageFile.getSize());
            Optional<Education> optionalEducation = educationRepository.findById(id);
            if (optionalEducation.isPresent()) {
                Education education = optionalEducation.get();
                education.setImage(imageUrl);
                educationRepository.save(education);
            } else {
                // Education with the provided ID does not exist
                throw new IOException("Education with provided Id does not exist");
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
