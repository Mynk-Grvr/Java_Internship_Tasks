package com.example.student.repository;

import com.example.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Task 4: Spring Data JPA Repository interface for Student Entity.
 * Provides out-of-the-box CRUD methods: save, findById, findAll, deleteById, existsById.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
