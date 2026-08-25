# Task 3: Database Integration using JDBC (MySQL Student Management System)

A production-style database-driven Java application for managing student records (`ID`, `Name`, `Age`). This project replaces file-based storage with relational database persistence using **Java Database Connectivity (JDBC)**, **SQL PreparedStatements**, and **ResultSets**.

---

## 📌 Features

1. **MySQL Database Integration**: Stores and queries records using JDBC connections.
2. **Full CRUD Operations**:
   - **Create (Add Student)**: Uses `PreparedStatement` with `INSERT INTO students`.
   - **Read (View All & Search)**: Fetches records using `SELECT * FROM students` and maps `ResultSet` to Java `Student` objects.
   - **Update (Modify Record)**: Updates student name and age using `UPDATE students SET name = ?, age = ? WHERE id = ?`.
   - **Delete (Remove Record)**: Deletes records safely using `DELETE FROM students WHERE id = ?`.
3. **Data Access Object (DAO) Pattern**: Decouples application interface logic (`StudentApp`) from database query execution (`StudentDAO`).

---

## 📂 Project Structure

```text
task_3-jdbc-student-management-system/
├── DBConnection.java    # JDBC connection manager for MySQL / Database setup
├── Student.java         # Student data model class
├── StudentDAO.java      # Data Access Object executing SQL queries
├── StudentApp.java      # Main interactive console application
├── schema.sql           # Database schema & initial SQL seed data
├── lib/                 # JDBC Driver dependencies (mysql-connector-j, h2)
└── README.md            # Project documentation & setup instructions
```

---

## 🗄️ Database Setup (`schema.sql`)

Execute the following SQL commands in MySQL Workbench or MySQL CLI:

```sql
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL
);

-- Seed Data
INSERT INTO students (id, name, age) VALUES (101, 'Alice Smith', 20);
INSERT INTO students (id, name, age) VALUES (102, 'Bob Johnson', 22);
```

---

## 🚀 How to Run the Project

### Prerequisites
- Java JDK 17 or higher installed.
- MySQL Server installed (or run with included JDBC driver bundle).

### Compilation & Execution Commands

1. **Navigate to the Project Folder**:
   ```bash
   cd task_3-jdbc-student-management-system
   ```

2. **Compile Java Source Files**:
   ```bash
   javac -cp ".;lib/*" Student.java DBConnection.java StudentDAO.java StudentApp.java
   ```

3. **Run the Main Application**:
   ```bash
   java -cp ".;lib/*" StudentApp
   ```

---

## 💻 Console Execution Output

```text
=================================================
  STUDENT MANAGEMENT SYSTEM (JDBC - DATABASE)    
=================================================

---------------- MAIN MENU ----------------
1. Add Student
2. View All Students
3. Search Student by ID
4. Update Student
5. Delete Student
6. Exit
Enter your choice (1-6): 1

--- Add New Student ---
Enter ID: 101
Enter Name: Alice Smith
Enter Age: 20
[SUCCESS] Student added successfully to database.

---------------- MAIN MENU ----------------
1. Add Student
2. View All Students
3. Search Student by ID
4. Update Student
5. Delete Student
6. Exit
Enter your choice (1-6): 2

--- All Student Records ---
-------------------------------------------------------
ID: 101    | Name: Alice Smith          | Age: 20   
-------------------------------------------------------
Total Database Records: 1
```

---

## 📖 Key Backend Concepts Learned

- **JDBC Architecture**: `DriverManager`, `Connection`, `PreparedStatement`, and `ResultSet`.
- **SQL Injection Prevention**: Using parameterized `PreparedStatement` queries (`?`) instead of string concatenation.
- **DAO Design Pattern**: Layering architecture into Entity -> DAO -> Controller/App.
