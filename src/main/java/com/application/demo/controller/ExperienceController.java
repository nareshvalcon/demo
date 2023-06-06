package com.application.demo.controller;

import java.util.ArrayList;
import java.util.List;
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
import com.application.demo.entity.Experience;
import com.application.demo.service.ExperienceService;
import com.application.demo.service.UserService;

@RestController
@RequestMapping("/api/experience")
public class ExperienceController {
    @Autowired
    private UserService userService;

    @Autowired
    private ExperienceService experienceService;

    @PostMapping("/add")
    public ResponseEntity<?> addExperience(@RequestParam("id") Long id, @RequestBody Experience experience) {
        Optional<AppUser> user = userService.getUserById(id);
        if(!user.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        experience.setUser(user.get());
        experience.setUserId(user.get().getId());
        Experience savedExperience = experienceService.saveExperience(experience);
        return ResponseEntity.ok(savedExperience);
    }

    @GetMapping("/get")
    public Experience getExperienceByUserId(@RequestParam("id") Long id) {
        Optional<Experience> experience = experienceService.getExperienceById(id);
        if(!experience.isPresent()){
            return null;
        }
        return experience.get();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadUserImage(@RequestParam("id") Long id, @RequestParam("image") MultipartFile image) {
        String imageUrl = experienceService.uploadImage(id, image);
        return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateExperience(@RequestParam Long id, @RequestBody Experience experienceUpdates) {
        Experience updatedExperience = experienceService.updateExperience(id, experienceUpdates);
        if(updatedExperience != null){
            return new ResponseEntity<>(updatedExperience, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Experience does not exist or Invalid ID");
    }
}
