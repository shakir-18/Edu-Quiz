package com.test.EduQuiz.repository;

import com.test.EduQuiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    List<Question> findTop5ByTopicIgnoreCaseAndDifficultyIgnoreCase(String topic, String difficulty);
}