package com.app.service;

import com.app.entity.StaffingRequest;
import com.app.repository.StaffingRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffingRequestService {

    private final StaffingRequestRepository repository;
    private final EmailService emailService;

    public StaffingRequest saveRequest(StaffingRequest request) {
        StaffingRequest savedRequest = repository.save(request);

        // Trigger email notification
        emailService.sendStaffingRequestEmail(savedRequest);

        return savedRequest;
    }

    public List<StaffingRequest> getAllRequests() {
        return repository.findAll();
    }

    public StaffingRequest getRequestById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public boolean deleteRequest(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
