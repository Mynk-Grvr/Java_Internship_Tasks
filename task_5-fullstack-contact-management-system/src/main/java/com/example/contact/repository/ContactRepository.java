package com.example.contact.repository;

import com.example.contact.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Contact entity.
 * Provides out-of-the-box JPA CRUD, sorting, and pagination methods.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * Search contacts by name or email with pagination & sorting.
     */
    Page<Contact> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nameQuery, String emailQuery, Pageable pageable
    );
}
