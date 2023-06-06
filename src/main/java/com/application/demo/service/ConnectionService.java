package com.application.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Connection;
import com.application.demo.repository.ConnectionRepository;
import com.application.demo.repository.UserRepository;
import com.application.demo.enumeration.ConnectionStatus;

@Service
public class ConnectionService {
    
    @Autowired
    private ConnectionRepository connectionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
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
        return connectionRepository.save(connection);
    }
    
    public List<Connection> getUserConnections(String userEmail) {
        Optional<AppUser> user = userRepository.findByEmail(userEmail);
        if(!user.isPresent()) {
            return null;
        }
        return connectionRepository.findByUser1OrUser2(user.get(), user.get());
    }

    public Connection acceptConnectionRequest(Long connectionId) {
        Optional<Connection> connection = connectionRepository.findById(connectionId);
    
        // if the connection doesn't exist or its status isn't REQUESTED, return null
        if (!connection.isPresent() || connection.get().getConnectionStatus() != ConnectionStatus.PENDING) {
            return null;
        }
    
        connection.get().setConnectionStatus(ConnectionStatus.CONFIRMED);
        connectionRepository.save(connection.get());
    
        return connection.get();
    }
    
    public Connection rejectConnectionRequest(Long connectionId) {
        Optional<Connection> connection = connectionRepository.findById(connectionId);
    
        // if the connection doesn't exist or its status isn't REQUESTED, return null
        if (!connection.isPresent() || connection.get().getConnectionStatus() != ConnectionStatus.PENDING) {
            return null;
        }
    
        connection.get().setConnectionStatus(ConnectionStatus.REJECTED);
        connectionRepository.save(connection.get());
    
        return connection.get();
    }
    
}
