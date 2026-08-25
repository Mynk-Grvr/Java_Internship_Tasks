# Task 4: Build REST APIs using Spring Boot (Student Management System)

A production-style backend RESTful Web API built using **Spring Boot**, **Spring Data JPA (Hibernate)**, and **MySQL / H2 Database**. This project converts the Student Management System into a modern REST API supporting full CRUD operations over HTTP endpoints.

---

## 📌 Features

1. **RESTful Architecture**: Exposes endpoints following standard HTTP methods (`GET`, `POST`, `PUT`, `DELETE`).
2. **Layered Software Design**:
   - **Controller Layer (`StudentController`)**: Exposes REST endpoints and handles HTTP request/response mappings.
   - **Service Layer (`StudentService`)**: Contains backend business logic.
   - **Repository Layer (`StudentRepository`)**: Interfaces with the database via Spring Data JPA.
   - **Entity Layer (`Student`)**: JPA mapped entity representing the `students` database table.
3. **Database Integration**: Built-in support for H2 embedded memory DB for zero-configuration testing and local MySQL server integration.

---

## 📂 Project Structure

```text
task_4-springboot-student-management-system/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/student/
│   │   │   ├── StudentApplication.java      # Spring Boot Entry Point
│   │   │   ├── controller/
│   │   │   │   └── StudentController.java   # REST Controller (@RestController)
│   │   │   ├── service/
│   │   │   │   └── StudentService.java      # Service Layer (@Service)
│   │   │   ├── repository/
│   │   │   │   └── StudentRepository.java   # JPA Repository Interface
│   │   │   └── model/
│   │   │       └── Student.java             # JPA Entity (@Entity)
│   │   └── resources/
│   │       └── application.properties       # DB & Server configuration
└── README.md
```

---

## 🌐 API Endpoint Documentation

| Method | Endpoint | Description | Request Body Example | HTTP Response Status |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/students` | Create new student record | `{"id": 101, "name": "Alice Smith", "age": 20}` | `201 Created` |
| **GET** | `/students` | Fetch list of all students | *None* | `200 OK` |
| **GET** | `/students/{id}` | Fetch student by ID | *None* | `200 OK` / `404 Not Found` |
| **PUT** | `/students/{id}` | Update student by ID | `{"name": "Alice Johnson", "age": 21}` | `200 OK` |
| **DELETE**| `/students/{id}` | Delete student by ID | *None* | `204 No Content` / `404 Not Found` |

---

## 🚀 How to Run the Spring Boot API

### Prerequisites
- Java JDK 17 or higher.
- Maven installed (`mvn` command available).

### Steps to Run

1. **Navigate to the Project Folder**:
   ```bash
   cd task_4-springboot-student-management-system
   ```

2. **Build and Package the Application**:
   ```bash
   mvn clean package -DskipTests
   ```

3. **Start the Spring Boot Server**:
   ```bash
   mvn spring-boot:run
   ```
   *(Server starts at `http://localhost:8080`)*

---

## 🧪 Testing the APIs using cURL / Postman

### 1. Add Student (POST)
```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"id": 101, "name": "Alice Smith", "age": 20}'
```

### 2. Get All Students (GET)
```bash
curl -X GET http://localhost:8080/students
```

### 3. Get Student by ID (GET)
```bash
curl -X GET http://localhost:8080/students/101
```

### 4. Update Student (PUT)
```bash
curl -X PUT http://localhost:8080/students/101 \
  -H "Content-Type: application/json" \
  -d '{"id": 101, "name": "Alice Johnson", "age": 21}'
```

### 5. Delete Student (DELETE)
```bash
curl -X DELETE http://localhost:8080/students/101
```

---

## 📖 Key Learnings Covered in Task 4

- **Spring Boot Framework**: Auto-configuration, dependency injection (`@Autowired`), component scanning.
- **RESTful API Standards**: JSON data format, HTTP verbs (`GET`, `POST`, `PUT`, `DELETE`), and proper status codes.
- **Spring Data JPA & ORM**: Automatic query generation without writing manual SQL.
