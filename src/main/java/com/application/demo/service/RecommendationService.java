package com.application.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.demo.entity.AppUser;
import com.application.demo.entity.Connection;
import com.application.demo.entity.Education;
import com.application.demo.entity.Experience;
import com.application.demo.repository.ConnectionRepository;
import com.application.demo.repository.UserRepository;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionRepository connectionRepository;
 
    public List<AppUser> recommendConnections(AppUser currentUser) {
        List<AppUser> recommendedUsers = new ArrayList<>();
        List<AppUser> defaultRecommendations = new ArrayList<>();
        List<Education> currentUserEducation = currentUser.getEducationList();
        List<Experience> currentUserExperience = currentUser.getExperienceList();
    
        // Retrieve all users
        List<AppUser> allUsers = userRepository.findAll();
    
        for (AppUser user : allUsers) {
            // Skip the current user
            if (user.getId().equals(currentUser.getId())) continue;
    
            // Skip if a connection already exists with the user
            if (connectionExists(currentUser, user)) continue;

            //Incase of no recommendations, recommend any two users
            defaultRecommendations.add(user);
            
            // Check for common education attributes
            for (Education edu : user.getEducationList()) {
                for (Education currEdu : currentUserEducation) {
                    if (edu.getUniversityName().equals(currEdu.getUniversityName()) ||
                        edu.getCourse().equals(currEdu.getCourse()) ||
                        isOverlap(edu.getStartYear(), edu.getEndYear(), currEdu.getStartYear(), currEdu.getEndYear())) {
                        if(!recommendedUsers.contains(user)){
                            recommendedUsers.add(user);
                        }
                        break;
                    }
                }
            }
    
            // Check for common experience attributes
            for (Experience exp : user.getExperienceList()) {
                for (Experience currExp : currentUserExperience) {
                    if (exp.getCompanyName().equals(currExp.getCompanyName()) ||
                        isOverlap(exp.getStartYear(), exp.getEndYear(), currExp.getStartYear(), currExp.getEndYear())) {
                        if(!recommendedUsers.contains(user)){
                            recommendedUsers.add(user);
                        }
                        break;
                    }
                }
            }
        }
        
        if(recommendedUsers.isEmpty()){
            for(AppUser defaultUser : defaultRecommendations){
                recommendedUsers.add(defaultUser);
            }
        }
        return recommendedUsers.size() <= 2 ? recommendedUsers  : recommendedUsers.subList(0, 2);
    }
    
    private boolean isOverlap(int startYear1, int endYear1, int startYear2, int endYear2) {
        return (startYear1 >= startYear2 && startYear1 <= endYear2) ||
               (endYear1 >= startYear2 && endYear1 <= endYear2) ||
               (startYear2 >= startYear1 && startYear2 <= endYear1) ||
               (endYear2 >= startYear1 && endYear2 <= endYear1);
    }

    public boolean connectionExists(AppUser user1, AppUser user2) {
        Optional<Connection> connection1 = connectionRepository.findByUser1AndUser2(user1, user2);
        Optional<Connection> connection2 = connectionRepository.findByUser1AndUser2(user2, user1);
        return connection1.isPresent() || connection2.isPresent();
    }    
}
