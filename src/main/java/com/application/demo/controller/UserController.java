package com.application.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Education;
import com.application.demo.entity.Email;
import com.application.demo.entity.Experience;
import com.application.demo.service.EducationService;
import com.application.demo.service.EmailService;
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

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@Valid @RequestBody AppUser user) {
        try{
            return ResponseEntity.ok(userService.registerUser(user));
        }
        catch(RuntimeException re){
            return new ResponseEntity(re.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        Optional<AppUser> loggedInUser = userService.loginUser(email, password);
        if(loggedInUser.isPresent()){
            return ResponseEntity.ok(loggedInUser.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        Optional<AppUser> user = userService.getUserByEmail(email);
        if(user.isPresent()) {
            Email mail = new Email();
            mail.setToEmail(user.get().getEmail());
            mail.setSubject("Forgot Password");
            mail.setBody("Click this link to reset your password: ...");
            try {
                emailService.sendEmail(mail);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>("Email could not be sent", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Reset password link sent to your email", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/confirm")
    public ResponseEntity<AppUser> confirmUser(@RequestParam("email") String email, @RequestParam("code") String confirmationCode) {
        return ResponseEntity.ok(userService.confirmUser(email, confirmationCode));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadUserImage(@RequestParam("id") String id, @RequestParam("image") MultipartFile image) {
        String imageUrl = userService.uploadImage(Long.valueOf(id), image);
        return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
    }

    @GetMapping("/getUser")
    public ResponseEntity<AppUser> getUserByEmail(@RequestParam("email") String email) {
        Optional<AppUser> user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping(path = "/education")
    public List<Education> getEducationByEmail(@RequestParam("id") Long id) {
        Optional<AppUser> user = userService.getUserById(id);
        if(!user.isPresent()){
            return new ArrayList<Education>();
        }
        return educationService.getEducationByUserId(user.get().getId());
    }

    @GetMapping("/experience")
    public List<Experience> getExperienceByUserId(@RequestParam("id") Long id) {
        Optional<AppUser> user = userService.getUserById(id);
        if(!user.isPresent()){
            return new ArrayList<Experience>();
        }
        return experienceService.getExperienceByUserId(user.get().getId());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam Long id, @RequestBody AppUser userUpdates) {
        AppUser updatedUser = userService.updateUser(id, userUpdates);
        if(updatedUser != null){
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist or Invalid ID"); 
    }
}
