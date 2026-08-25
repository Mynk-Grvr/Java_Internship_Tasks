package com.example.student.service;

import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Task 4: Service layer encapsulating business logic for Student Management System.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Save a new student entity.
     */
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Retrieve all student entities.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Retrieve student by ID.
     */
    public Optional<Student> getStudentById(int id) {
        return studentRepository.findById(id);
    }

    /**
     * Update an existing student entity by ID.
     */
    public Student updateStudent(int id, Student studentDetails) {
        return studentRepository.findById(id).map(existing -> {
            existing.setName(studentDetails.getName());
            existing.setAge(studentDetails.getAge());
            return studentRepository.save(existing);
        }).orElseGet(() -> {
            studentDetails.setId(id);
            return studentRepository.save(studentDetails);
        });
    }

    /**
     * Delete student by ID.
     */
    public boolean deleteStudent(int id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
