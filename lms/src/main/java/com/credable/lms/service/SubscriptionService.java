package com.credable.lms.service;

import com.credable.lms.domain.CustomerSubscription;
import com.credable.lms.exception.BadRequestException;
import com.credable.lms.repository.CustomerSubscriptionRepository;
import com.credable.lms.wsdl.customer.CustomerResponse;
import com.credable.lms.external.soap.CustomerKYC;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.credable.lms.wsdl.customer.Status;
import java.time.LocalDateTime;

@Service
public class SubscriptionService {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);
    
    private final CustomerSubscriptionRepository subscriptionRepository;
    private final CustomerKYC kycCustomer;

    public SubscriptionService(CustomerSubscriptionRepository subscriptionRepository,
                             CustomerKYC kycCustomer) {
        this.subscriptionRepository = subscriptionRepository;
        this.kycCustomer = kycCustomer;
    }

    @Transactional
    public CustomerSubscription subscribeCustomer(Long customerId) {
        logger.info("Processing subscription request for customer: {}", customerId);

        // Check if customer is already subscribed
        if (subscriptionRepository.existsById(customerId)) {
            logger.warn("Customer {} is already subscribed", customerId);
            throw new BadRequestException("Customer is already subscribed");
        }

        // Verify KYC status
        CustomerResponse kycResponse = verifyKYC(customerId);
        logger.debug("Raw KYC response: {}", kycResponse);
        
        if (kycResponse == null) {
            logger.warn("KYC verification failed - null response received for customer: {}", customerId);
            throw new BadRequestException("KYC verification failed - no response received");
        }

        if (kycResponse.getCustomer() == null) {
            logger.warn("KYC verification failed - null customer data in response for customer: {}", customerId);
            throw new BadRequestException("KYC verification failed - no customer data in response");
        }

        // Check if customer is active
        Status status = kycResponse.getCustomer().getStatus();
        logger.debug("Customer status: {}", status);
        
        if (status == null || status != Status.ACTIVE) {
            logger.warn("KYC verification failed - customer status is not ACTIVE: {} for customer: {}", status, customerId);
            throw new BadRequestException("KYC verification failed - customer is not active");
        }

        // Create new subscription
        CustomerSubscription subscription = new CustomerSubscription();
        subscription.setCustomerId(customerId);
        subscription.setSubscriptionDate(LocalDateTime.now());
        subscription.setStatus(true);

        logger.info("Customer {} subscribed successfully", customerId);
        return subscriptionRepository.save(subscription);
    }

    private CustomerResponse verifyKYC(Long customerNumber) {
        logger.debug("Verifying KYC for customer: {}", customerNumber);
        try {
            CustomerResponse response = kycCustomer.getCustomerKYCData(customerNumber.toString());
            logger.debug("KYC verification response received for customer: {}", customerNumber);
            return response;
        } catch (Exception e) {
            logger.error("Error during KYC verification for customer {}: {}", customerNumber, e.getMessage());
            throw new BadRequestException("Error during KYC verification: " + e.getMessage());
        }
    }

    public boolean isCustomerSubscribed(Long customerId) {
        return subscriptionRepository.existsById(customerId);
    }
} 