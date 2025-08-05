package com.test.EduQuiz.service;

import com.test.EduQuiz.entity.Student;
import com.test.EduQuiz.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class WeeklyReportService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private StudentRepository studentRepository;


    @Scheduled(cron = "0 0 9 * * SUN")
    public void sendWeeklyReports()
    {
        List<Student> studentList= studentRepository.findAll();
        for(Student student: studentList)
        {
            String email= student.getEmail();
            if(email==null || email.isBlank())continue;
            String report=generateReport(student);
            emailService.sendEmail(email,"Your Weekly Quiz Report.",report);
        }
    }
    public String generateReport(Student student)
    {
        return String.format("Hi %s,\n\nHere is your weekly quiz report:\n" +
                "- Total Attempts: %d\n" +
                "- Total Score: %d\n" +
                "- Average Score: %.2f\n\nKeep practicing!",student.getName(),student.getTotal_attempts(),
                student.getTotal_score(),student.getAvg_score());
    }
}