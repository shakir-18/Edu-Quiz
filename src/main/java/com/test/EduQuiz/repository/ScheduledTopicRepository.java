package com.test.EduQuiz.repository;

import com.test.EduQuiz.entity.ScheduledTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduledTopicRepository extends JpaRepository<ScheduledTopic,Integer> {
    ScheduledTopic findByScheduledForDateAndQuizCreated(LocalDate date, boolean created);
    boolean existsByScheduledForDate(LocalDate date);
}
