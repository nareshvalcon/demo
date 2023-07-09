package com.application.demo.repository;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    Optional<List<Connection>> findByUser1OrUser2(AppUser user1, AppUser user2);
    @Query("SELECT c FROM Connection c WHERE (c.user1 = :user1 AND c.user2 = :user2) OR (c.user1 = :user2 AND c.user2 = :user1)")
    Optional<Connection> findByUser1AndUser2(@Param("user1") AppUser user1, @Param("user2") AppUser user2);
    Optional<Connection> findById(Long id);
}

