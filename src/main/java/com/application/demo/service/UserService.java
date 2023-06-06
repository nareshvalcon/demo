package com.application.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.demo.entity.AppUser;
import com.application.demo.exception.UnauthorizedException;
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
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists.");
        }
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

    public Optional<AppUser> loginUser(String email, String password) {
        Optional<AppUser> user = userRepository.findByEmail(email);
        
        if (!user.isPresent() || !password.equals(user.get().getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        } 
        return user;
    }

    public AppUser confirmUser(String email, String confirmationCode) {
        Optional<AppUser> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getConfirmationCode().equals(confirmationCode)) {
            user.get().setVerified(true);
            userRepository.save(user.get());
        }
        return user.get();
    }

    public String uploadImage(Long id, MultipartFile imageFile) {
        String blobName = generateBlobName(id, imageFile.getOriginalFilename());
        String imageUrl;
        try (InputStream data = imageFile.getInputStream()) {
            imageUrl = azureStorageService.upload(blobName, data, imageFile.getSize());
            Optional<AppUser> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                AppUser user = optionalUser.get();
                user.setImage(imageUrl);
                userRepository.save(user);
            } else {
                // User with the provided ID does not exist
                throw new IOException("User with provided Id does not exist");
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

    public Optional<AppUser> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public AppUser updateUser(Long id, AppUser userUpdates) {
        Optional<AppUser> user = userRepository.findById(id);
        if(user.isPresent()){
            AppUser userOld = user.get();
            userOld.setFirstName(userUpdates.getFirstName() != null ? userUpdates.getFirstName() : userOld.getFirstName());
            userOld.setMiddleName(userUpdates.getMiddleName() != null ? userUpdates.getMiddleName() : userOld.getMiddleName());
            userOld.setLastName(userUpdates.getLastName() != null ? userUpdates.getLastName() : userOld.getLastName());
            userOld.setPassword(userUpdates.getPassword() != null ? userUpdates.getPassword() : userOld.getPassword());
            return userRepository.save(userOld);
        }
        return null;
                    
    }
    
}

