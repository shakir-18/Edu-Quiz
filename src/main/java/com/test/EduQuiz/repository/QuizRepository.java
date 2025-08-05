package com.test.EduQuiz.repository;

import com.test.EduQuiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Integer> {
    List<Quiz> findByEndTimeAfter(LocalDateTime now);
    Quiz findTop1ByEndTimeBefore(LocalDateTime now);
}
