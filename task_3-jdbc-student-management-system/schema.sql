-- Task 3: Database Integration using JDBC
-- Database setup script for Student Management System

-- Create Database
CREATE DATABASE IF NOT EXISTS student_db;

-- Use Database
USE student_db;

-- Create Students Table
CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL
);

-- Insert Sample Initial Data
INSERT INTO students (id, name, age) VALUES (101, 'Alice Smith', 20);
INSERT INTO students (id, name, age) VALUES (102, 'Bob Johnson', 22);
INSERT INTO students (id, name, age) VALUES (103, 'Carol Williams', 21);
