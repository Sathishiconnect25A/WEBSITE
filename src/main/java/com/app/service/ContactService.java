package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entity.ContactMessage;
import com.app.repository.ContactMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository repository;
    private final EmailService emailService;   // ✅ inject EmailService

    // ⭐ SAVE CONTACT MESSAGE + SEND EMAIL
    public ContactMessage saveMessage(ContactMessage message) {

        // 1️⃣ Save to database first
        ContactMessage savedMessage = repository.save(message);

        // 2️⃣ Trigger SUPPORT email (non-blocking)
        try {
            emailService.sendEmail(
                    "SUPPORT",                      // ✅ applicationType
                    message.getFullName(),
                    message.getPhone(),
                    message.getEmail(),
                    "-",                            // qualification (not applicable)
                    "-",                            // year / experience
                    message.getService(),           // service / enquiry type
                    message.getMessage(),           // message content
                    null                            // no attachment
            );
        } catch (Exception e) {
            // ❌ Email failure should NOT affect contact save
            System.err.println("Support email failed: " + e.getMessage());
        }

        return savedMessage;
    }

    // 🔍 GET ALL
    public List<ContactMessage> getAllMessages() {
        return repository.findAll();
    }

    // 🔍 GET BY ID
    public ContactMessage getMessageById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Contact Message Not Found: " + id));
    }

    // ❌ DELETE
    public boolean deleteMessage(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
