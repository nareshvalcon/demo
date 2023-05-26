package com.application.demo.entity;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.application.demo.validator.EducationDateValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Company name is required")
    private String companyName;
    private String role;

    @NotEmpty(message = "Start Year is required")
    private int startYear;
    private int endYear; //optional
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    private AppUser appUser;

    // getters and setters
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public String getCompanyName(){
        return this.companyName;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }

    public int getStartYear(){
        return this.startYear;
    }

    public void setStartYear(int startYear){
        this.startYear = startYear;
    }

    public void setEndYear(int endYear){
        this.endYear = endYear;
    }
    
    public int getEndYear(){
        return this.endYear;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public String getImage(){
        return this.imageUrl;
    }

    public void setImage(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setUser(AppUser user){
        this.appUser = user;
    }
    
    @JsonIgnore
    public AppUser getUser(){
        return this.appUser;
    }
}

