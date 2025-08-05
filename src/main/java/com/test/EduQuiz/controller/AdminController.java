package com.test.EduQuiz.controller;


import com.test.EduQuiz.entity.ScheduledTopic;
import com.test.EduQuiz.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    //-----STUDENT-----
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers()
    {
        return adminService.getAllUsers();
    }
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id)
    {
        return adminService.getUserById(id);
    }
    @DeleteMapping("deleteUserById/{id}")
    public ResponseEntity<?> deleteUserByRollNo(@PathVariable String rollNo)
    {
        return adminService.deleteUserByRollNo(rollNo);
    }


    //-----QUIZ-----
    @PostMapping("/createQuiz/{topic}/{difficulty}")
    public ResponseEntity<?> createQuiz(@PathVariable String topic, @PathVariable String difficulty)
    {
        return adminService.createQuiz(topic,difficulty);
    }
    @GetMapping("/getQuizByQuizId/{id}")
    public ResponseEntity<?> getQuizByQuizId(@PathVariable int id)
    {
        return adminService.getQuizByQuizId(id);
    }
    @PostMapping("/scheduleQuiz")
    public ResponseEntity<?> scheduleTopic(@Valid @RequestBody ScheduledTopic topic)
    {
        return adminService.scheduleTopic(topic);
    }


    //-----STATISTICS-----
    @GetMapping("/topByQuizId/{id}")
    public ResponseEntity<?> topByQuizId(@PathVariable int id)
    {
        return adminService.topByQuizId(id);
    }
    @GetMapping("/topByQuizTopic/{topic}")
    public ResponseEntity<?> topByQuizTopic(@PathVariable String topic)
    {
        return adminService.topByQuizTopic(topic);
    }
    @GetMapping("topByDifficulty/{difficulty}")
    public ResponseEntity<?> topByQuizDifficulty(@PathVariable String difficulty)
    {
        return adminService.topByQuizDifficulty(difficulty);
    }
    @PostMapping("/uploadQuestions")
    public ResponseEntity<?> uploadQuestions(@RequestParam("file") MultipartFile multipartFile)
    {
        return adminService.uploadQuestions(multipartFile);
    }
}