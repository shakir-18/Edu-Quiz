package com.test.EduQuiz.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizSession {
    private String studentId;
    private int quizId;
    private LocalDateTime startTime;
    private List<String> userAnswers=new ArrayList<>();
    private boolean submitted=false;

    public void setUserAnswers(List<String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public List<String> getUserAnswers() {
        return userAnswers;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
