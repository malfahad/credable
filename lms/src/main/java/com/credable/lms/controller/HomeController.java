package com.credable.lms.controller;

import com.credable.lms.repository.ClientConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final ClientConfigRepository clientConfigRepository;

    public HomeController(ClientConfigRepository clientConfigRepository) {
        this.clientConfigRepository = clientConfigRepository;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getStatus() {
        logger.debug("Received request for API status");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "LMS API is running");
        response.put("version", "1.0.0");
        
        // Registration status
        boolean isRegistered = clientConfigRepository.count() > 0;
        response.put("clientRegistration", Map.of(
            "status", isRegistered ? "registered" : "not registered",
            "message", isRegistered ? "Client is registered with scoring service" : "Client registration pending"
        ));
        
        logger.info("API status check completed. Client registration status: {}", isRegistered ? "registered" : "not registered");
        return ResponseEntity.ok(response);
    }
} 