package com.test.EduQuiz.repository;

import com.test.EduQuiz.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    Optional<Student> findByRollNo(String rollNo);
    boolean existsByRollNo(String rollNo);
    void deleteByRollNo(String rollNo);
}
