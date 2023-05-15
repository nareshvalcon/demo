package com.application.demo.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.demo.entity.AppUser;
import com.application.demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUser registerUser(AppUser user) {
        // Encrypt the password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // Generate confirmation code
        String code = String.format("%04d", new Random().nextInt(10000));
        user.setConfirmationCode(code);

        // Save user to the database
        userRepository.save(user);

        // Send confirmation email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirmation Code");
        message.setText("Your confirmation code is: " + code);
        mailSender.send(message);

        return user;
    }

    public AppUser confirmUser(String confirmationCode) {
        AppUser user = userRepository.findByConfirmationCode(confirmationCode);
        if (user != null) {
            user.setConfirmationCode(null);
            userRepository.save(user);
        }
        return user;
    }
}

