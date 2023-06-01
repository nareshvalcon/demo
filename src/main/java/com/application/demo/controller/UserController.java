package com.application.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Education;
import com.application.demo.entity.Experience;
import com.application.demo.service.EducationService;
import com.application.demo.service.ExperienceService;
import com.application.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EducationService educationService;

    @Autowired
    private ExperienceService experienceService;


    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@Valid @RequestBody AppUser user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/confirm")
    public ResponseEntity<AppUser> confirmUser(@RequestParam("code") String confirmationCode) {
        return ResponseEntity.ok(userService.confirmUser(confirmationCode));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadUserImage(@RequestParam("id") Long id, @RequestParam("image") MultipartFile image) {
        String imageUrl = userService.uploadImage(id, image);
        return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
    }

    @GetMapping("/getUser")
    public ResponseEntity<AppUser> getUserByEmail(@RequestParam("email") String email) {
        AppUser user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/education")
    public ResponseEntity<?> addEducation(@RequestParam("email") String email, @RequestBody Education education) {
        AppUser user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        education.setUser(user);
        Education savedEducation = educationService.saveEducation(education);
        
        return ResponseEntity.ok(savedEducation);
    }

    @GetMapping(path = "/education")
    public List<Education> getEducationByEmail(@RequestParam("email") String email) {
        AppUser user = userService.getUserByEmail(email);
        if (user == null) {
            return new ArrayList<Education>();
        }
        return educationService.getEducationByUserId(user.getId());
    }

    @PostMapping("/experience")
    public ResponseEntity<?> addExperience(@RequestParam("email") String email, @RequestBody Experience experience) {
        AppUser user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        experience.setUser(user);
        Experience savedExperience = experienceService.saveExperience(experience);
        return ResponseEntity.ok(savedExperience);
    }

    @GetMapping("/experience")
    public List<Experience> getExperienceByUserEmail(@RequestParam String email) {
        AppUser user = userService.getUserByEmail(email);
        if (user == null) {
            return new ArrayList<Experience>();
        }
        return experienceService.getExperienceByUserId(user.getId());
    }

}
