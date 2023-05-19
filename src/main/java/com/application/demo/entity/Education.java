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

import com.application.demo.validator.EducationDateValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@EducationDateValidator
public class Education implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String universityName;
    private String degree;
    private int startYear;
    private int endYear; //optional

    @Column(name = "email")
    private String email;

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
