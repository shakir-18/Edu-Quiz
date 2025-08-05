package com.test.EduQuiz.dtos;

import java.util.List;

public class UserAnswers {
    private List<String> userAnswers;

    public void setUserAnswers(List<String> userAnswers) {
        this.userAnswers = userAnswers;
    }
    public List<String> getUserAnswers() {
        return userAnswers;
    }
}