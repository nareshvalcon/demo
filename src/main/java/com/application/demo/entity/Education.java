package com.application.demo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.application.demo.validator.EducationDateValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EducationDateValidator
public class Education implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "University name is required")
    private String universityName;

    @NotEmpty(message = "Degree is required")
    private String degree;

    @NotEmpty(message = "Course is required")
    private String course;

    @NotEmpty(message = "Start year is required")
    private int startYear;
    private int endYear; //optional

    @Column(name = "email")
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    private AppUser appUser;

    //getters and setters
    public void setUniversityName(String universityName){
        this.universityName = universityName;
    }

    public String getUniversityName(){
        return this.universityName;
    }

    public void setDegree(String degree){
        this.degree = degree;
    }

    public String getDegree(){
        return this.degree;
    }

    public void setCourse(String course){
        this.course = course;
    }

    public String getCourse(){
        return this.course;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getImage(){
        return this.imageUrl;
    }

    public void setImage(String imageUrl){
        this.imageUrl = imageUrl;
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

    public void setUser(AppUser user){
        this.appUser = user;
    }
    
    @JsonIgnore
    public AppUser getUser(){
        return this.appUser;
    }
}
