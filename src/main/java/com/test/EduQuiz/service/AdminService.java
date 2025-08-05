package com.test.EduQuiz.service;


import com.test.EduQuiz.entity.*;
import com.test.EduQuiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private AttemptRepository attemptRepository;
    @Autowired
    private ScheduledTopicRepository scheduledTopicRepository;
    @Autowired
    private EmailService emailService;

    //-----STUDENT-----
    public ResponseEntity<?> getAllUsers()
    {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentRepository.findAll());
    }
    public ResponseEntity<?> getUserById(int id)
    {
        Student student=studentRepository.findById(id).orElse(null);
        if (student != null)
        {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(student);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO STUDENT WITH ID "+id);
        }
    }
    public ResponseEntity<?> deleteUserByRollNo(String rollNo)
    {

        Student student=studentRepository.findByRollNo(rollNo).orElse(null);
        if (student != null)
        {
            studentRepository.deleteByRollNo(rollNo);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("STUDENT DELETED SUCCESSFULLY");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO STUDENT WITH ROLL.NO "+rollNo);
        }
    }

    //-----QUIZ-----
    public ResponseEntity<?> createQuiz(String topic, String difficulty)
    {
        Quiz quiz=new Quiz();
        List<Question> questions=questionRepository.findTop5ByTopicIgnoreCaseAndDifficultyIgnoreCase(topic,difficulty);
        if(questions != null &&  questions.size()!=0) {
            quiz.setDifficulty(difficulty);
            quiz.setTopic(topic);
            quiz.setQuestions(questions);
            quiz.setStartTime(LocalDateTime.now());
            quiz.setEndTime(LocalDateTime.now().plusHours(10));
            quizRepository.save(quiz);
            return ResponseEntity.status(HttpStatus.CREATED).body("QUIZ WITH ID " + quiz.getId()  +
                    " IS CREATED SUCCESSFULLY");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CREATED).body("QUIZ CREATION FAILED FOR DESCRIBED CRITERIA !!!");
        }
    }
    public ResponseEntity<?> getQuizByQuizId(int id)
    {
        Quiz quiz=quizRepository.findById(id).orElse(null);
        if(quiz!=null)
        {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(quiz);
        }
        return ResponseEntity.status(HttpStatus.OK).body("NO QUIZ WITH QUIZ ID "+id);
    }
    public ResponseEntity<?> scheduleTopic(ScheduledTopic topic)
    {
        if (!scheduledTopicRepository.existsByScheduledForDate(topic.getScheduledForDate())) {
            scheduledTopicRepository.save(topic);
            return ResponseEntity.ok("Topic scheduled.");
        }
        else
        {
            return ResponseEntity.ok("Quiz is already scheduled for the given date!!!");
        }
    }
    @Scheduled(cron = "0 0 9 * * *")
    public void autoCreateQuizzes()
    {
        LocalDate today=LocalDate.now();
        ScheduledTopic topic= scheduledTopicRepository.findByScheduledForDateAndQuizCreated(today,false);
        if(topic!=null) {
            List<Question> questions = questionRepository.findTop5ByTopicIgnoreCaseAndDifficultyIgnoreCase(
                    topic.getTopic(), topic.getDifficulty());
            if (questions.size() < 5) {
                emailService.sendEmail("admin@gmail.com",
                        "Quiz Generation Failed",
                        "No enough questions for topic: " + topic.getTopic() + ", difficulty: " + topic.getDifficulty());
                return;
            }
            Quiz quiz = new Quiz();
            quiz.setTopic(topic.getTopic());
            quiz.setDifficulty(topic.getDifficulty());
            quiz.setStartTime(LocalDateTime.now());
            quiz.setEndTime(LocalDateTime.now().plusHours(10));
            quiz.setQuestions(questions);
            quizRepository.save(quiz);
            topic.setCreated(true);
            scheduledTopicRepository.save(topic);
            System.out.println("QUIZ SCHEDULED");
        }
        else
        {
            emailService.sendEmail("admin@gmail.com",
                    "No active quiz scheduled",
                    "No active quiz scheduled");
        }
    }
    @Scheduled (cron = "0 0 10 * * *")
    public void displayResults()
    {
        Quiz quiz= quizRepository.findTop1ByEndTimeBefore(LocalDateTime.now());
        if(quiz!=null) {
            List<Attempt> attempts = attemptRepository.findByQuizId(quiz.getId());
            attempts.sort(Comparator.comparing(Attempt::getScore).reversed());
            Map<String, Integer> scores = new HashMap<>();
            for (Attempt attempt : attempts) {
                scores.put(attempt.getStudent().getName(), attempt.getScore());
            }
            emailService.sendEmail("admin@gmail.com", "Result of quiz " + quiz.getId(), scores.toString());
        }
    }
    @Scheduled(cron = "0 0 * * 0")
    public void sendPerformanceMails()
    {
        List<Student> students=studentRepository.findAll();
        for(Student student:students)
        {
            String performance="Total Attempts : "+student.getTotal_attempts()+"\n"
                    +"Total Score : "+student.getTotal_score()
                    +"Average Score : "+student.getAvg_score()+"\n";
            emailService.sendEmail(student.getEmail(),"Performance statistics",performance);
        }
    }


    //-----STATISTICS-----
    public ResponseEntity<?> topByQuizId(int id)
    {
        Quiz quiz=quizRepository.findById(id).orElse(null);
        if(quiz != null) {
            List<Attempt> attempts = attemptRepository.findByQuizId(id);
            attempts.sort(Comparator.comparing(Attempt::getScore).reversed());
            Map<String, Integer> scores = new HashMap<>();
            for (Attempt attempt : attempts) {
                scores.put(attempt.getStudent().getName(), attempt.getScore());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(scores);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO QUIZ WITH ID "+id);
        }
    }
    public ResponseEntity<?> topByQuizTopic(String topic) {
        List<Attempt> attempts = attemptRepository.findByTopic(topic);
        if(attempts.size()==0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH TOPIC OR NO QUIZZES ON THAT TOPIC!!!");
        }
        attempts.sort(Comparator.comparing(Attempt::getScore).reversed());
        Map<String, Integer> scores = new HashMap<>();
        for (Attempt attempt : attempts) {
            scores.put(attempt.getStudent().getName(), attempt.getScore());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(scores);
    }
    public ResponseEntity<?> topByQuizDifficulty(String difficulty)
    {
        List<Attempt> attempts = attemptRepository.findByDifficulty(difficulty);
        if(attempts.size()==0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH TOPIC OR NO QUIZZES ON THAT TOPIC!!!");
        }
        attempts.sort(Comparator.comparing(Attempt::getScore).reversed());
        Map<String, Integer> scores = new HashMap<>();
        for (Attempt attempt : attempts) {
            scores.put(attempt.getStudent().getName(), attempt.getScore());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(scores);
    }
    public ResponseEntity<?> uploadQuestions(MultipartFile multipartFile)
    {
        if(multipartFile.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("FILE IS EMPTY");
        }
        try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))){
                String line;
                boolean isFirst=true;
                List<Question> questions=new ArrayList<>();
                while((line=bufferedReader.readLine())!=null)
                {
                    if(isFirst)
                    {
                        isFirst=false;
                        continue;
                    }
                    String[] fields=line.split(",",-1);
                    if(fields.length<8)
                    {
                        continue;
                    }
                    Question question=new Question();
                    question.setTopic(fields[0].trim());
                    question.setDifficulty(fields[1].trim());
                    question.setQuestionText(fields[2].trim());
                    question.setOption1(fields[3].trim());
                    question.setOption2(fields[4].trim());
                    question.setOption3(fields[5].trim());
                    question.setOption4(fields[6].trim());
                    question.setCorrect(fields[7].trim());
                    questions.add(question);
                }
                questionRepository.saveAll(questions);
                return ResponseEntity.ok("Uploaded and saved " + questions.size() + " questions.");
        }
        catch (IOException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file.");
        }
    }
}