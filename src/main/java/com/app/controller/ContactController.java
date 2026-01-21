package com.app.controller;

import com.app.dto.ContactMessageDTO;
import com.app.entity.ContactMessage;
import com.app.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContactController {

    private final ContactService service;

    @PostMapping("/send")
    public ResponseEntity<?> submitContact(@RequestBody ContactMessageDTO dto) {

        ContactMessage message = ContactMessage.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .company(dto.getCompany())
                .phone(dto.getPhone())
                .service(dto.getService())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .build();

        return ResponseEntity.ok(service.saveMessage(message));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllMessages());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getById(@RequestParam Long id) {
        return ResponseEntity.ok(service.getMessageById(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        if (service.deleteMessage(id))
            return ResponseEntity.ok("Deleted successfully");

        return ResponseEntity.notFound().build();
    }
}
