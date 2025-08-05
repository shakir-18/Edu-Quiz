package com.test.EduQuiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,name = "roll_no",nullable = false)
    private String rollNo;
    private String name;
    @Column(nullable = false)
    private String email;
    private String password;
    private String role="STUDENT";
    private int total_score=0;
    private int total_attempts=0;
    private float avg_score=0;
    @OneToMany
    @JsonIgnore
    private List<Attempt> attempts=new ArrayList<>();

    public void setRoll_no(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setAttempts(List<Attempt> attempts) {
        this.attempts = attempts;
    }

    public List<Attempt> getAttempts() {
        return attempts;
    }

    public int getId() {
        return id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public void setAvg_score(float avg_score) {
        this.avg_score = avg_score;
    }

    public float getAvg_score() {
        return avg_score;
    }

    public void setTotal_attempts(int total_attempts) {
        this.total_attempts = total_attempts;
    }

    public int getTotal_attempts() {
        return total_attempts;
    }
}