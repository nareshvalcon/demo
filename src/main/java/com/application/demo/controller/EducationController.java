package com.application.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Education;
import com.application.demo.service.EducationService;
import com.application.demo.service.UserService;

@RestController
@RequestMapping("/api/education")
public class EducationController {
    @Autowired
    private UserService userService;

    @Autowired
    private EducationService educationService;

    @PostMapping("/add")
    public ResponseEntity<?> addEducation(@RequestParam("id") Long id, @RequestBody Education education) {
        Optional<AppUser> user = userService.getUserById(id);
        if(!user.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        education.setUser(user.get());
        education.setUserId(user.get().getId());
        Education savedEducation = educationService.saveEducation(education);
        
        return ResponseEntity.ok(savedEducation);
    }

    @GetMapping(path = "/get")
    public Education getEducationById(@RequestParam("id") Long id) {
        Optional<Education> education = educationService.getEducationById(id);
        if(!education.isPresent()){
            return null;
        }
        return education.get();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("id") Long id, @RequestParam("image") MultipartFile image) {
        String imageUrl = educationService.uploadImage(id, image);
        return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEducation(@RequestParam Long id, @RequestBody Education educationUpdates) {
        Education updatedEducation = educationService.updateEducation(id, educationUpdates);
        if(updatedEducation != null){
            return new ResponseEntity<>(updatedEducation, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Education does not exist or Invalid ID");
    
    }
}
