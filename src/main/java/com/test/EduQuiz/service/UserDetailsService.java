package com.test.EduQuiz.service;

import com.test.EduQuiz.entity.Student;
import com.test.EduQuiz.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String rollNo) throws UsernameNotFoundException {
        Student student = studentRepository.findByRollNo(rollNo)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Ensure role is not null and prefixed with "ROLE_"
        String role = student.getRole();
        if (role == null || role.isBlank()) {
            role = "ROLE_USER"; // default fallback
        } else if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return new org.springframework.security.core.userdetails.User(
                student.getRollNo(),
                student.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}