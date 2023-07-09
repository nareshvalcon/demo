package com.application.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Connection;
import com.application.demo.repository.ConnectionRepository;
import com.application.demo.repository.UserRepository;
import com.application.demo.enumeration.ConnectionStatus;
import com.application.demo.enumeration.UserConnectionStatus;

@Service
public class ConnectionService {
    
    @Autowired
    private ConnectionRepository connectionRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecommendationService recommendationService;
    
    public Connection sendConnectionRequest(String user1Email, String user2Email) {
        Optional<AppUser> user1 = userRepository.findByEmail(user1Email);
        Optional<AppUser> user2 = userRepository.findByEmail(user2Email);
        
        if(!user1.isPresent() || !user2.isPresent()) {
            return null;
        }

        Connection connection = new Connection();
        connection.setUser1(user1.get());
        connection.setUser2(user2.get());
        connection.setConnectionStatus(ConnectionStatus.PENDING);
        connection.setStatusUser1(UserConnectionStatus.PENDING);
        connection.setStatusUser2(UserConnectionStatus.PENDING);
        return connectionRepository.save(connection);
    }
    
    public List<Connection> getUserConnections(String userEmail) {
        Optional<AppUser> user = userRepository.findByEmail(userEmail);
        if(!user.isPresent()) {
            return null;
        }
        Optional<List<Connection>> connections = connectionRepository.findByUser1OrUser2(user.get(), user.get());
        if(connections.isPresent()){
            return connections.get();
        }
        return null;
    }

    public Connection acceptConnectionRequest(String senderEmail, String recepientEmail) {
        Optional<AppUser> user1 = userRepository.findByEmail(senderEmail);
        Optional<AppUser> user2 = userRepository.findByEmail(recepientEmail);
        if(user1.isPresent() && user2.isPresent()){
            Optional<Connection> connection = connectionRepository.findByUser1AndUser2(user1.get(), user2.get());
        
            // if the connection doesn't exist or its status isn't REQUESTED, return null
            if (!connection.isPresent() || connection.get().getConnectionStatus() != ConnectionStatus.PENDING) {
                return null;
            }
            
            if(Objects.equals(connection.get().getUser1().getId(), user1.get().getId())){
                connection.get().setStatusUser1(UserConnectionStatus.LIKED);
                if(connection.get().getStatusUser2() == UserConnectionStatus.LIKED){
                    connection.get().setConnectionStatus(ConnectionStatus.CONFIRMED);
                }
                else{
                    connection.get().setConnectionStatus(ConnectionStatus.PENDING);
                }
            }
            else if(Objects.equals(connection.get().getUser2().getId(), user1.get().getId())){
                connection.get().setStatusUser2(UserConnectionStatus.LIKED);
                if(connection.get().getStatusUser1() == UserConnectionStatus.LIKED){
                    connection.get().setConnectionStatus(ConnectionStatus.CONFIRMED);
                }
                else{
                    connection.get().setConnectionStatus(ConnectionStatus.PENDING);
                }
            }
            connectionRepository.save(connection.get());
            return connection.get();
        }
        return null;
    }
    
    public Connection rejectConnectionRequest(String senderEmail, String recepientEmail) {
        Optional<AppUser> user1 = userRepository.findByEmail(senderEmail);
        Optional<AppUser> user2 = userRepository.findByEmail(recepientEmail);
        if(user1.isPresent() && user2.isPresent()){
            Optional<Connection> connection = connectionRepository.findByUser1AndUser2(user1.get(), user2.get());
        
            // if the connection doesn't exist or its status isn't REQUESTED, return null
            if (!connection.isPresent() || connection.get().getConnectionStatus() != ConnectionStatus.PENDING) {
                return null;
            }
        
            if(Objects.equals(connection.get().getUser1().getId(), user1.get().getId())){
                connection.get().setStatusUser1(UserConnectionStatus.DISLIKED);
                if(connection.get().getStatusUser2() == UserConnectionStatus.LIKED){
                    connection.get().setConnectionStatus(ConnectionStatus.REJECTED);
                }
                else{
                    connection.get().setConnectionStatus(ConnectionStatus.PENDING);
                }
            }
            else if(Objects.equals(connection.get().getUser2().getId(), user1.get().getId())){
                connection.get().setStatusUser2(UserConnectionStatus.DISLIKED);
                if(connection.get().getStatusUser1() == UserConnectionStatus.LIKED){
                    connection.get().setConnectionStatus(ConnectionStatus.REJECTED);
                }
                else{
                    connection.get().setConnectionStatus(ConnectionStatus.PENDING);
                }
            }
            connectionRepository.save(connection.get());
        
            return connection.get();
        }
        return null;
    }

    public List<AppUser> recommendConnections(AppUser currentUser) {
        List<AppUser> recommendations = recommendationService.recommendConnections(currentUser);
        addConnections(currentUser, recommendations);
        return recommendations;
    }

    private void addConnections(AppUser currentUser, List<AppUser> recommendations){
        for (AppUser appUser : recommendations) {
            sendConnectionRequest(currentUser.getEmail(), appUser.getEmail());
        }
    }   
}
