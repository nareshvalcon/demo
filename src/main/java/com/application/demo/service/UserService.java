package com.application.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.demo.entity.AppUser;
import com.application.demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AzureStorageService azureStorageService;

    // @Autowired
    // private EmailService emailService;

    public AppUser registerUser(AppUser user) {
        user.setPassword(user.getPassword());
        // Generate confirmation code
        String code = String.format("%04d", new Random().nextInt(10000));
        user.setConfirmationCode(code);

        user.setVerified(false);

        // Save user to the database
        userRepository.save(user);

        // Send confirmation email
        // String to = user.getEmail();
        // String subject = "Confirmation code to login";
        // String body = "Thank you for registering. Here is your confirmation code." + code;
        // emailService.sendEmail(to, subject, body);

        return user;
    }

    public AppUser confirmUser(String confirmationCode) {
        AppUser user = userRepository.findByConfirmationCode(confirmationCode);
        if (user != null) {
            user.setVerified(true);
            userRepository.save(user);
        }
        return user;
    }

    public String uploadImage(String email, MultipartFile imageFile) {
        String blobName = generateBlobName(email, imageFile.getOriginalFilename());
        String imageUrl;
        try (InputStream data = imageFile.getInputStream()) {
            imageUrl = azureStorageService.upload(blobName, data, imageFile.getSize());
            AppUser user = userRepository.findByEmail(email);
            if(user != null){
                user.setImage(imageUrl);
                userRepository.save(user);
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

    private String generateBlobName(String email, String originalFilename) {
        // Generate a unique blob name using the email and original filename
        return email + "/" + UUID.randomUUID() + "_" + originalFilename;
    }
}

