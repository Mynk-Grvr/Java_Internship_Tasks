package com.example.contact.controller;

import com.example.contact.dto.ContactRequest;
import com.example.contact.model.Contact;
import com.example.contact.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Managing Contact Inquiries and Support Tickets.
 * Base Path: /contacts
 */
@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * POST /contacts : Create new contact entry (validated with Bean Validation)
     */
    @PostMapping
    public ResponseEntity<Contact> createContact(@Valid @RequestBody ContactRequest request) {
        Contact created = contactService.createContact(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * GET /contacts : Browse contacts with Pagination, Sorting & Keyword Search
     * Query Params: page (default 0), size (default 10), sort (default createdAt,desc), search (optional)
     */
    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) String search) {
        Page<Contact> contactPage = contactService.getContacts(page, size, sort, search);
        return ResponseEntity.ok(contactPage);
    }

    /**
     * GET /contacts/stats : Summary counts for the whole queue.
     * Declared before the /{id} mapping below; Spring matches the literal
     * path in preference to the path variable, so "stats" is never
     * mistaken for an identifier.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(contactService.getStatusCounts());
    }

    /**
     * GET /contacts/{id} : Fetch single contact by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        return contactService.getContactById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /contacts/{id} : Update contact details or status (PENDING / RESOLVED)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @Valid @RequestBody ContactRequest request) {
        return contactService.updateContact(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /contacts/{id} : Delete contact record
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        boolean deleted = contactService.deleteContact(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /contacts/export : Export all contacts to CSV
     */
    @GetMapping("/export")
    public void exportCsv(jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"contacts.csv\"");
        contactService.exportToCsv(response.getWriter());
    }

    /**
     * POST /contacts/import : Import contacts from CSV
     */
    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            contactService.importFromCsv(file);
            return ResponseEntity.ok("CSV imported successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import CSV: " + e.getMessage());
        }
    }
}