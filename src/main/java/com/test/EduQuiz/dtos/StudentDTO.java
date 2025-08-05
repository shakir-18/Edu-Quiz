package com.test.EduQuiz.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentDTO {
    @NotBlank(message = "Roll No cannot be empty")
    private String rollNo;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @Email(message = "Invalid Email")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
}
