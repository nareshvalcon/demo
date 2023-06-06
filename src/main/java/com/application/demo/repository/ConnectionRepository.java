package com.application.demo.repository;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByUser1OrUser2(AppUser user1, AppUser user2);
    Optional<Connection> findById(Long id);
}

