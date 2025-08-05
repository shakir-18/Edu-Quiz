package com.test.EduQuiz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class ScheduledTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Topic Cannot Be Empty")
    private String topic;
    @NotBlank(message = "Difficulty Cannot Be Empty")
    private String difficulty;
    @NotNull(message = "Date Cannot Be Empty")
    @Column(nullable = false)
    private LocalDate scheduledForDate;
    private boolean quizCreated;

    public int getId() {
        return id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setScheduledForDate(LocalDate scheduledForDate) {
        this.scheduledForDate = scheduledForDate;
    }

    public LocalDate getScheduledForDate() {
        return scheduledForDate;
    }

    public void setCreated(boolean created) {
        this.quizCreated = created;
    }

    public boolean isCreated() {
        return quizCreated;
    }
}
