package com.application.demo.entity;


import java.io.Serializable;

import javax.persistence.*;

import com.application.demo.enumeration.ConnectionStatus;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}))
public class Connection implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private AppUser user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private AppUser user2;

    @Enumerated(EnumType.STRING)
    private ConnectionStatus status;

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setUser1(AppUser user1){
        this.user1 = user1;
    }

    public AppUser getUser1(){
        return this.user1;
    }

    public void setUser2(AppUser user2){
        this.user2 = user2;
    }

    public AppUser getUser2(){
        return this.user2;
    }

    public void setConnectionStatus(ConnectionStatus status){
        this.status = status;
    }

    public ConnectionStatus getConnectionStatus(){
        return this.status;
    }
}
