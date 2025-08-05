package com.test.EduQuiz.controller;

import com.test.EduQuiz.dtos.StudentDTO;
import com.test.EduQuiz.dtos.UserAnswers;
import com.test.EduQuiz.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody StudentDTO studentDTO)
    {
        return studentService.register(studentDTO.getRollNo(),studentDTO.getEmail(),studentDTO.getName(),studentDTO.getPassword());
    }
    @GetMapping("/login")
    public ResponseEntity<?> login(Authentication authentication)
    {
        return studentService.login(authentication);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody StudentDTO studentDTO,Authentication authentication)
    {
        return studentService.update(studentDTO.getName(),studentDTO.getPassword(),authentication);
    }
    @GetMapping("/checkUpdates")
    public ResponseEntity<?> checkUpdates(Authentication authentication)
    {
        return studentService.checkUpdates(authentication);
    }
    @GetMapping("/quiz/{id}")
    public ResponseEntity<?> playQuiz(@PathVariable int id, Authentication authentication)
    {
        return studentService.playQuiz(id,authentication);
    }
    @PostMapping("/quiz/{id}")
    public ResponseEntity<?> submitQuiz(@PathVariable int id, @RequestBody UserAnswers userAnswers,Authentication authentication)
    {
        List<String> answers=userAnswers.getUserAnswers();
        return studentService.submitQuiz(id,answers,authentication);
    }
}