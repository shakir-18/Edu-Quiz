package com.test.EduQuiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EduQuizApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduQuizApplication.class, args);
	}

}