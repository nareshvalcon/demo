package com.application.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.application.demo.entity.*;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByConfirmationCode(String confirmationCode);
}

