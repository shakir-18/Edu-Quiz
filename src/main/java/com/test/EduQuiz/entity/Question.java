package com.test.EduQuiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String questionText;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    @JsonIgnore
    private String correct;
    private String difficulty;
    private String topic;
    @ManyToMany(mappedBy = "questions")
    @JsonIgnore
    private List<Quiz> quizzes;

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    @JsonIgnore
    public int getId() {
        return id;
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

    @JsonIgnore
    public String getTopic() {
        return topic;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption4() {
        return option4;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption1() {
        return option1;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getCorrect() {
        return correct;
    }
}