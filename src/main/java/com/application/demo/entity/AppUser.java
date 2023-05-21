package com.application.demo.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class AppUser implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    private boolean verified;
    
    private String confirmationCode;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Education> educationList;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Experience> experienceList;

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
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
}

