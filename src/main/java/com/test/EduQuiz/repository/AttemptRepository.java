package com.test.EduQuiz.repository;

import com.test.EduQuiz.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt,Integer> {
    List<Attempt> findByQuizId(int id);
    List<Attempt> findByTopic(String topic);
    List<Attempt> findByDifficulty(String difficulty);
    Optional<Attempt> findByQuizIdAndStudentId(int quizId, int studentId);
}
