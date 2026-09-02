# Task 5: Secure Java Full Stack Contact Management System

A production-grade, full-stack **Contact Management System & Support Ticket Dashboard** built with **Spring Boot 3**, **Spring Security (RBAC)**, **Spring Data JPA**, **Bean Validation**, **Global Exception Handling**, and a responsive **Frontend UI**.

---

## 📌 Key Objectives & Features

1. **Full-Stack End-to-End CRUD Architecture**:
   - **Create**: Add new contact entries manually (validated server-side with Bean Validation).
   - **Read**: Browse contacts with **Pagination & Sorting** (`Pageable`, `Page<Contact>`).
   - **Update**: Edit existing records (correct spelling, update message content, change status `PENDING` / `RESOLVED`).
   - **Delete**: Remove contacts with a confirmation modal overlay.
2. **Spring Security & Authorization**:
   - Role-Based Access Control (RBAC) protecting `/contacts` management endpoints for `ADMIN` role.
   - Built-in admin credentials: `admin` / `admin123`.
3. **Bean Validation & Global Exception Handling**:
   - Sanitizes inputs using `@NotBlank`, `@Email`, `@Size`.
   - `@RestControllerAdvice` converts `MethodArgumentNotValidException` into clean JSON field error maps without 500 server stack traces.
4. **Interactive Dashboard UI**:
   - Responsive Data Table with real-time status badges.
   - Debounced keyword search filter (search by name or email).
   - Live validation feedback inside modal forms.
   - Pagination controls (Previous / Next / Page counter).

---

## 📂 Project Structure

```text
task_5-fullstack-contact-management-system/
├── pom.xml                                  # Maven dependencies (Spring Boot, Security, JPA, Validation, H2)
├── src/
│   ├── main/
│   │   ├── java/com/example/contact/
│   │   │   ├── ContactApplication.java      # Main Entry Point
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java      # Spring Security RBAC Configuration
│   │   │   ├── controller/
│   │   │   │   └── ContactController.java   # REST Endpoints (/contacts)
│   │   │   ├── dto/
│   │   │   │   └── ContactRequest.java      # DTO with Bean Validation Annotations
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java # @RestControllerAdvice Validation Exception Handler
│   │   │   ├── model/
│   │   │   │   └── Contact.java             # JPA Entity mapped to 'contacts' table
│   │   │   ├── repository/
│   │   │   │   └── ContactRepository.java   # JpaRepository interface with search support
│   │   │   └── service/
│   │   │       └── ContactService.java      # Service Layer (Business Logic & Pagination)
│   │   └── resources/
│   │       ├── application.properties       # Server port, security credentials, & DB config
│   │       ├── schema.sql                   # SQL Table creation script
│   │       └── static/                      # Frontend Single Page App Assets
│   │           ├── index.html               # Main Dashboard View
│   │           ├── styles.css               # Clean Modern Stylesheet
│   │           └── app.js                   # REST API Fetch & UI Event Handlers
└── README.md
```

---

## 🌐 REST API Specifications

| Method | Endpoint | Authorization | Description | Request Body / Query Params | Status Code |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **POST** | `/contacts` | Public / Admin | Create new contact entry | `{"name":"John", "email":"john@example.com", "message":"Inquiry"}` | `201 Created` |
| **GET** | `/contacts` | `ADMIN` | List contacts (Paginated & Sorted) | `?page=0&size=10&sort=createdAt,desc&search=john` | `200 OK` |
| **GET** | `/contacts/{id}` | `ADMIN` | Fetch single contact | *None* | `200 OK` / `404 Not Found` |
| **PUT** | `/contacts/{id}` | `ADMIN` | Update contact & status | `{"name":"John Doe", "email":"john@example.com", "message":"Updated", "status":"RESOLVED"}` | `200 OK` |
| **DELETE**| `/contacts/{id}` | `ADMIN` | Delete contact entry | *None* | `204 No Content` |

---

## 🚀 How to Run the Full-Stack Application

### Prerequisites
- Java JDK 17 or higher.
- Maven installed (`mvn` command available).

### Steps to Run

1. **Navigate to the Project Directory**:
   ```bash
   cd task_5-fullstack-contact-management-system
   ```

2. **Build the Application**:
   ```bash
   mvn clean package -DskipTests
   ```

3. **Start the Spring Boot Server**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the Web Dashboard**:
   - Open your browser to: **`http://localhost:8080`**
   - Use default Admin credentials:
     - **Username**: `admin`
     - **Password**: `admin123`

---

## 📖 Key Learnings Demonstrated in Task 5

- **Bean Validation Framework**: Utilizing `@NotBlank`, `@Email`, and custom error messaging.
- **Spring Security Integration**: Authenticating endpoints with Role-Based Access Control.
- **Enterprise Pagination & Sorting**: Leveraging Spring Data JPA `Pageable` and `Page<T>` abstractions.
- **Global Exception Handling**: Returning uniform JSON error maps via `@RestControllerAdvice`.
- **Full-Stack Coordination**: Connecting modern JavaScript fetch clients with Spring Boot REST backends.
