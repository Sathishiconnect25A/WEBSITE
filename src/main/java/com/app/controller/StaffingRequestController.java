package com.app.controller;

import com.app.dto.StaffingRequestDTO;
import com.app.entity.StaffingRequest;
import com.app.service.StaffingRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staffing")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StaffingRequestController {

    private final StaffingRequestService service;

    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestBody StaffingRequestDTO dto) {
        StaffingRequest request = StaffingRequest.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .companyName(dto.getCompanyName())
                .phoneNumber(dto.getPhoneNumber())
                .staffingNeeds(dto.getStaffingNeeds())
                .build();

        return ResponseEntity.ok(service.saveRequest(request));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getRequestById(@RequestParam Long id) {
        StaffingRequest request = service.getRequestById(id);
        if (request != null) {
            return ResponseEntity.ok(request);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRequest(@RequestParam Long id) {
        if (service.deleteRequest(id)) {
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
