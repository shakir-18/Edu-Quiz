package com.test.EduQuiz.service;

import com.test.EduQuiz.dtos.QuizSession;
import com.test.EduQuiz.dtos.UserAnswers;
import com.test.EduQuiz.entity.Attempt;
import com.test.EduQuiz.entity.Question;
import com.test.EduQuiz.entity.Quiz;
import com.test.EduQuiz.entity.Student;
import com.test.EduQuiz.repository.AttemptRepository;
import com.test.EduQuiz.repository.QuestionRepository;
import com.test.EduQuiz.repository.QuizRepository;
import com.test.EduQuiz.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AttemptRepository attemptRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(String roll_no, String email, String name, String password)
    {
        if(studentRepository.existsByRollNo(roll_no))
        {
            throw new DataIntegrityViolationException("Roll.No already in use.");
        }
        Student student=new Student();
        student.setRoll_no(roll_no);
        student.setName(name);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body("USER CREATED SUCCESSFULLY");
    }

    public ResponseEntity<?> login(Authentication authentication) throws UsernameNotFoundException
    {
        if(authentication==null || !authentication.isAuthenticated())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("INVALID CREDENTIALS");
        }
        Student student=studentRepository.findByRollNo(authentication.getName()).orElseThrow(()->
                new UsernameNotFoundException("STUDENT WITH ROLL.NO "+authentication.getName()+" NOT FOUND"));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(student);
    }

    public ResponseEntity<?> update(String name, String password, Authentication authentication)
    {
        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
        {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("SESSION EXPIRED! LOGIN AGAIN");
        }
        String rollNo=authentication.getName();
        Student student=studentRepository.findByRollNo(rollNo).orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND!"));
        student.setName(name);
        student.setPassword(passwordEncoder.encode(password));
        studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body("USER UPDATED SUCCESSFULLY");
    }

    public ResponseEntity<?> checkUpdates(Authentication authentication) throws RuntimeException
    {
        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
        {
            throw new RuntimeException("SESSION EXPIRED! LOGIN AGAIN.");
        }
        String rollNo=authentication.getName();
        Student student=studentRepository.findByRollNo(rollNo).orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND!"));
        List<Quiz> quiz=quizRepository.findByEndTimeAfter(LocalDateTime.now());
        String ans="";
        int i=1;
        for(Quiz q:quiz)
        {
            ans=ans + i+".Quiz ---> id: "+q.getId()+"   Topic : "+q.getTopic()+"   Difficulty : "+q.getDifficulty()+"\n\n";
            i++;
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ans);
    }
    private final Map<String , QuizSession> activeSessions=new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler= Executors.newScheduledThreadPool(10);

    public ResponseEntity<?> playQuiz(int id,Authentication authentication)
    {
        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
        {
            throw new RuntimeException("SESSION EXPIRED! LOGIN AGAIN.");
        }
        Student student = studentRepository.findByRollNo(authentication.getName()).orElseThrow(()->
                new UsernameNotFoundException("USER NOT FOUND!"));
        Quiz quiz=quizRepository.findById(id).orElse(null);
        if(student!=null && quiz!=null)
        {
            Optional<Attempt> attempt=attemptRepository.findByQuizIdAndStudentId(quiz.getId(),student.getId());
            if(attempt.isPresent())
            {
                throw  new IllegalStateException("YOU HAVE ALREADY ATTEMPTED THIS QUIZ");
            }
        }
        if(quiz!=null)
        {
            QuizSession quizSession=new QuizSession();
            quizSession.setQuizId(id);
            quizSession.setStudentId(authentication.getName());
            quizSession.setStartTime(LocalDateTime.now());
            quizSession.setSubmitted(false);
            activeSessions.put(authentication.getName(),quizSession);
            scheduler.schedule(()-> autoSubmit(quizSession.getStudentId()),150, TimeUnit.SECONDS);
            return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CREATED).body("NO QUIZ WITH QUIZ ID "+id);
        }
    }
    public ResponseEntity<?> submitQuiz(int id, List<String> userAnswers,Authentication authentication)
    {
        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
        {
            throw new RuntimeException("SESSION EXPIRED! LOGIN AGAIN.");
        }
        Student student = studentRepository.findByRollNo(authentication.getName()).orElseThrow(()->
                new UsernameNotFoundException("USER NOT FOUND!"));
        QuizSession quizSession=activeSessions.get(authentication.getName());
        if(quizSession ==null || quizSession.isSubmitted())
        {
            return ResponseEntity.status(HttpStatus.GONE).body("Quiz already submitted or expired or does not exist!");
        }
        quizSession.setUserAnswers(userAnswers);
        quizSession.setSubmitted(true);
        ResponseEntity<?> response=processSubmission(quizSession);
        activeSessions.remove(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
    }
    public ResponseEntity<?> processSubmission(QuizSession quizSession)
    {
        Quiz quiz=quizRepository.findById(quizSession.getQuizId()).orElse(null);
        Student student=studentRepository.findByRollNo(quizSession.getStudentId()).orElse(null);
        if(quiz==null || student==null)
        {
            return ResponseEntity.status(HttpStatus.GONE).body("NO ACTIVE QUIZ SESSIONS!");
        }
        List<Question> questions=quiz.getQuestions();
        List<String> correctAnswers=new ArrayList<>();
        for (Question question : questions) {
            correctAnswers.add(question.getCorrect());
        }
        int score=0;
        List<String> answers=quizSession.getUserAnswers();
        if(answers==null || answers.size()==0)
        {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("NO ANSWERS PROVIDED!");
        }
        int min=Math.min(correctAnswers.size(),answers.size());
        for(int i=0;i<min;i++)
        {
            if(answers.get(i)!=null) {
                if (correctAnswers.get(i).equalsIgnoreCase(answers.get(i))) {
                    score++;
                }
            }
        }
        Attempt attempt=new Attempt();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setDifficulty(quiz.getDifficulty());
        attempt.setTopic(quiz.getTopic());
        attempt.setScore(score);
        attemptRepository.save(attempt);
        student.getAttempts().add(attempt);
        student.setTotal_attempts(student.getTotal_attempts()+1);
        student.setTotal_score(student.getTotal_score()+score);
        student.setAvg_score((float)student.getTotal_score()/student.getTotal_attempts());
        studentRepository.save(student);
        quiz.getAttempts().add(attempt);
        quizRepository.save(quiz);
        return ResponseEntity.status(HttpStatus.CREATED).body("QUIZ SUCCESSFULLY COMPLETED!" +
                " THE SCORE IS "+score);
    }
    public void autoSubmit(String studentId)
    {
        QuizSession quizSession=activeSessions.get(studentId);
        if(quizSession!=null && !quizSession.isSubmitted())
        {
            quizSession.setSubmitted(true);
            ResponseEntity<?> response = processSubmission(quizSession);
            activeSessions.remove(studentId);
            System.out.println(response.getBody());
            System.out.println("Auto-submitted quiz for student: " + studentId);
        }
    }
}