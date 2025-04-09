package com.credable.lms.controller;

import com.credable.lms.domain.CustomerSubscription;
import com.credable.lms.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{customerId}/subscribe")
    public ResponseEntity<CustomerSubscription> subscribeCustomer(@PathVariable Long customerId) {
        CustomerSubscription subscription = subscriptionService.subscribeCustomer(customerId);
        return ResponseEntity.ok(subscription);
    }

    @GetMapping("/{customerId}/subscription")
    public ResponseEntity<Boolean> isSubscribed(@PathVariable Long customerId) {
        boolean isSubscribed = subscriptionService.isCustomerSubscribed(customerId);
        return ResponseEntity.ok(isSubscribed);
    }
} 