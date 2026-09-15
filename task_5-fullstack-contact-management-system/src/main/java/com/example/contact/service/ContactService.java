package com.example.contact.service;

import com.example.contact.dto.ContactRequest;
import com.example.contact.model.Contact;
import com.example.contact.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service layer encapsulating business logic, pagination, search, and CRUD workflows.
 */
@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Create a new contact entry.
     */
    public Contact createContact(ContactRequest request) {
        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setMessage(request.getMessage());
        contact.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");
        return contactRepository.save(contact);
    }

    /**
     * Retrieve paginated and sorted contacts with optional keyword search.
     */
    public Page<Contact> getContacts(int page, int size, String sort, String search) {
        // Parse sort argument e.g. "name,asc" or "createdAt,desc"
        Sort sortOrder = Sort.by("createdAt").descending();
        if (sort != null && !sort.trim().isEmpty()) {
            String[] parts = sort.split(",");
            String field = parts[0].trim();
            boolean isAsc = parts.length > 1 && parts[1].trim().equalsIgnoreCase("asc");
            sortOrder = isAsc ? Sort.by(field).ascending() : Sort.by(field).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        if (search != null && !search.trim().isEmpty()) {
            String query = search.trim();
            return contactRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, pageable);
        }

        return contactRepository.findAll(pageable);
    }

    /**
     * Summary counts for the whole queue, not merely the page in view.
     * Returned as a map so that a further state can be added without
     * altering the response shape.
     */
    public Map<String, Long> getStatusCounts() {
        Map<String, Long> counts = new LinkedHashMap<>();
        long pending = contactRepository.countByStatus("PENDING");
        long resolved = contactRepository.countByStatus("RESOLVED");
        counts.put("pending", pending);
        counts.put("resolved", resolved);
        counts.put("total", contactRepository.count());
        return counts;
    }

    /**
     * Retrieve single contact by ID.
     */
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    /**
     * Update existing contact record.
     */
    public Optional<Contact> updateContact(Long id, ContactRequest request) {
        return contactRepository.findById(id).map(existing -> {
            existing.setName(request.getName());
            existing.setEmail(request.getEmail());
            existing.setMessage(request.getMessage());
            if (request.getStatus() != null) {
                existing.setStatus(request.getStatus());
            }
            return contactRepository.save(existing);
        });
    }

    /**
     * Delete contact by ID.
     */
    public boolean deleteContact(Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return true;
        }
        return false;
    }
}