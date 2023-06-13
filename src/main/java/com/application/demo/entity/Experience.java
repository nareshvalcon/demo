package com.application.demo.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Experience implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Company name is required")
    private String companyName;
    private String role;

    @NotNull(message = "Start Year is required")
    private int startYear;
    private int endYear; //optional
    private String description;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private AppUser appUser;

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

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

    // public void setEmail(String email){
    //     this.email = email;
    // }

    // public String getEmail(){
    //     return this.email;
    // }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public Long getUserId(){
        return this.userId;
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

