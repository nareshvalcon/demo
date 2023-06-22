package com.application.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Connection;
import com.application.demo.service.ConnectionService;
import com.application.demo.service.RecommendationService;
import com.application.demo.service.UserService;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
    @Autowired
    private ConnectionService connectionService; // this service needs to be implemented

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendationService recommendationService;

    // Endpoint to send a connection request from user1 to user2
    @PostMapping("/request")
    public ResponseEntity<?> sendConnectionRequest(@RequestParam("user1") String user1Email, @RequestParam("user2") String user2Email) {
        Connection newConnection = connectionService.sendConnectionRequest(user1Email, user2Email);
        if (newConnection == null) {
            return new ResponseEntity<>("Failed to send connection request", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newConnection, HttpStatus.CREATED);
    }

    // Endpoint to fetch all connections of user1
    @GetMapping("/getConnections")
    public ResponseEntity<?> getUserConnections(@RequestParam("email") String userEmail) {
        List<Connection> connections = connectionService.getUserConnections(userEmail);
        return new ResponseEntity<>(connections, HttpStatus.OK);
    }

    // Endpoint to accept a connection request
    @PostMapping("/accept")
    public ResponseEntity<?> acceptConnectionRequest(@RequestParam("connectionId") Long connectionId) {
        Connection connection = connectionService.acceptConnectionRequest(connectionId);
        if (connection == null) {
            return new ResponseEntity<>("Failed to accept connection request", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(connection, HttpStatus.OK);
    }

    // Endpoint to reject a connection request
    @PostMapping("/reject")
    public ResponseEntity<?> rejectConnectionRequest(@RequestParam("connectionId") Long connectionId) {
        Connection connection = connectionService.rejectConnectionRequest(connectionId);
        if (connection == null) {
            return new ResponseEntity<>("Failed to reject connection request", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(connection, HttpStatus.OK);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestParam("userid") Long userId) {
        Optional<AppUser> currentUser = userService.getUserById(userId);
        if(currentUser.isPresent()){
            List<AppUser> recommendations = connectionService.recommendConnections(currentUser.get());
            return ResponseEntity.ok(recommendations);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + userId + " not found.");
    }

}

