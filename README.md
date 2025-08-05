# 🎓 EduQuiz — Spring Boot Based Quiz Platform

EduQuiz is a **Java Spring Boot** application designed to manage quizzes for students, including scheduling, scoring, real-time quiz attempts, and email reporting. Built with clean architecture principles, robust validation, and production-ready features.

---

## 🚀 Features

- ✅ **User Registration & Login** (Basic Auth + BCrypt encrypted passwords)
- ✅ **Role-Based Access** for Admin and Students
- ✅ **Timed Quizzes** (auto-submit after 150 seconds)
- ✅ **CSV Upload of Questions** by Admin
- ✅ **Scheduled Quiz Creation** (daily/weekly) via `@Scheduled`
- ✅ **Weekly Email Reports** to students using `JavaMailSender`
- ✅ **Score Calculation and Leaderboard**
- ✅ **Global Exception Handling** with validation responses
- ✅ **Quiz Attempt Restrictions** (prevent reattempts)
- ✅ **Question filtering** by topic/difficulty (case-insensitive)
- ✅ **Environment variables for DB & Email credentials**
- ✅ **Code structured using Controller → Service → Repository layers**

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **JavaMailSender**
- **Scheduled Tasks**
- **Maven**

---

## 📂 Project Structure

```
com.test.EduQuiz
│
├── controller          # REST endpoints(Admin and Student)
├── service             # Business logic
├── repository          # JPA repositories
├── entity              # JPA entities (Quiz, Student, Attempt...)
├── dtos                # Data Transfer Objects
├── config              # Security configuration
└── exception           # Global exception handling
```

---

## ⚙️ Getting Started

### 1. Clone the Repo

```bash
git clone https://github.com/shakir-18/Edu-Quiz.git
cd Edu-Quiz
```

### 2. Create `application.properties` (or use the example)

Copy the provided example file:

```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

Then edit `application.properties` and add your values:

```properties
spring.datasource.url=jdbc:mysql://<your-cloud-sql-host>:3306/<your-db-name>
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

✅ Use [Gmail App Passwords](https://support.google.com/mail/answer/185833) (if using Gmail).

---

### 3. Run the App

```bash
./mvnw spring-boot:run
```

The app will start at `http://localhost:8080`

---

## 📬 API Highlights

| Endpoint            | Method | Description                      |
| ------------------- | ------ | -------------------------------- |
| `/student/register` | POST   | Register a student               |
| `/student/login`    | GET    | Login with Basic Auth            |
| `/quiz/{id}/`       | GET    | Start a quiz (150s timer starts) |
| `/quiz/{id}/`       | POST   | Submit quiz answers              |
| `/admin/upload`     | POST   | Upload questions via CSV         |
| `/admin/schedule`   | POST   | Schedule a quiz for future       |

---

## 📦 Deployment

The app is designed for **Render** or any other cloud platform.

- Use environment variables like `${DB_USERNAME}` and `${MAIL_PASSWORD}`.
- Cloud SQL: Use PlanetScale, Railway, or AWS RDS for MySQL.
- App must be **running continuously** for `@Scheduled` features to trigger.

---

## 📄 Additional Notes

- 📌 Email sending is done via `JavaMailSender` (weekly reports & admin alerts).
- 🧪 Basic testing can be done via Postman or Swagger UI (add `springdoc-openapi` if needed).
- 🔐 Ensure `.gitignore` excludes sensitive files (`target/`, `.idea/`, etc.).

---

## 👤 Author

**Mohammed Shakir Ahmed**

GitHub: [shakir-18](https://github.com/shakir-18)

---

## 🏁 Final Note

This project demonstrates:

- Real-world use of Spring Boot
- Clean coding and architecture
- Functional scheduler and mailing systems
- End-to-end quiz management system

> 🌟 Ready to deploy & showcase in your resume or interview!

---
