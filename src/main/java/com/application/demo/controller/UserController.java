package com.application.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.application.demo.entity.AppUser;
import com.application.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@Valid @RequestBody AppUser user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/confirm")
    public ResponseEntity<AppUser> confirmUser(@RequestParam("code") String confirmationCode) {
        return ResponseEntity.ok(userService.confirmUser(confirmationCode));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadUserImage(@RequestParam("email") String email, @RequestParam("image") MultipartFile image) {
        String imageUrl = userService.uploadImage(email, image);
        return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
    }

}
