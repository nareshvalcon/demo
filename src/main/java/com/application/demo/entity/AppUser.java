package com.application.demo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class AppUser implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true)
    private Long id;
    
    @NotEmpty(message = "First Name is required")
    private String firstName;

    private String middleName;

    @NotEmpty(message = "Last Name is required")
    private String lastName;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email is not valid")
    @Column(unique = true)
    private String email;

    private boolean verified;
    
    private String confirmationCode;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Education> educationList;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Experience> experienceList;

    // @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    // private List<Connection> connectionsInitiated;

    // @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    // private List<Connection> connectionsReceived;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Connection> connectionsInitiated = new ArrayList<Connection>();

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Connection> connectionsReceived = new ArrayList<Connection>();

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getMiddleName(){
        return this.middleName;
    }

    public void setMiddleName(String middleName){
        this.middleName = middleName;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getConfirmationCode(){
        return this.confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode){
        this.confirmationCode = confirmationCode;
    }

    public String getImage(){
        return this.imageUrl;
    }

    public void setImage(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public Boolean getVerified(){
        return this.verified;
    }

    public void setVerified(Boolean verified){
        this.verified = verified;
    }

    public List<Education> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<Education> educationList) {
        this.educationList = educationList;
    }

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    public List<Connection> getConnectionsInitiated() {
        return connectionsInitiated;
    }

    public void setConnectionsInitiated(List<Connection> connectionsInitiated) {
        this.connectionsInitiated = connectionsInitiated;
    }

    public List<Connection> getConnectionsReceived() {
        return connectionsReceived;
    }

    public void setConnectionsReceived(List<Connection> connectionsReceived) {
        this.connectionsReceived = connectionsReceived;
    }
    
}

