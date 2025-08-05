package com.test.EduQuiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToMany
    @JoinTable(name="quiz_questions",
    joinColumns = @JoinColumn(name = "quiz_id"),
    inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questions;
    private String topic;
    private String difficulty;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @OneToMany
    @JsonIgnore
    private List<Attempt> attempts;

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public int getId() {
        return id;
    }

    public void setAttempts(List<Attempt> attempts) {
        this.attempts = attempts;
    }

    public List<Attempt> getAttempts() {
        return attempts;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}