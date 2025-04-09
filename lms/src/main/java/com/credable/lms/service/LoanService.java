package com.credable.lms.service;

import com.credable.lms.domain.LoanRequest;
import com.credable.lms.domain.LoanStatus;
import com.credable.lms.exception.BadRequestException;
import com.credable.lms.exception.NotFoundException;
import com.credable.lms.repository.LoanRequestRepository;
import com.credable.lms.repository.CustomerSubscriptionRepository;
import com.credable.lms.external.rest.ScoringAPIService;
import com.credable.lms.domain.CreditReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class LoanService {
    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
    
    private final LoanRequestRepository loanRequestRepository;
    private final CustomerSubscriptionRepository subscriptionRepository;
    private final ScoringAPIService scoringAPIService;

    public LoanService(LoanRequestRepository loanRequestRepository,
                      CustomerSubscriptionRepository subscriptionRepository,
                      ScoringAPIService scoringAPIService) {
        this.loanRequestRepository = loanRequestRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.scoringAPIService = scoringAPIService;
    }

    @Transactional
    public LoanRequest requestLoan(Long customerId, BigDecimal amount) {
        logger.info("Processing loan request for customer: {}, amount: {}", customerId, amount);

        // Check for pending loan requests
        if (loanRequestRepository.existsByCustomerIdAndStatus(customerId, LoanStatus.PENDING)) {
            logger.warn("Customer {} has a pending loan request", customerId);
            throw new BadRequestException("You have a pending loan request");
        }

        // Check customer subscription
        if (!subscriptionRepository.existsById(customerId)) {
            logger.warn("Customer {} is not subscribed", customerId);
            throw new BadRequestException("Customer is not subscribed");
        }

        // Initiate credit score request
        String scoreToken = scoringAPIService.initiateCreditScoreRequest(customerId);
        CreditReport creditReport = scoringAPIService.queryCreditScore(customerId, scoreToken);
        
        // Create new loan request
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setCustomerId(customerId);
        loanRequest.setAmount(amount);
        loanRequest.setStatus(LoanStatus.PENDING);
        loanRequest.setRequestDate(LocalDateTime.now());

        // Check credit report
        if (!creditReport.getExclusion().equals("No Exclusion")) {
            logger.trace("Customer {} has a credit exclusion: {}", customerId, creditReport.getExclusion());
            loanRequest.setStatus(LoanStatus.REJECTED);
            loanRequest.setRejectionReason("Credit exclusion: " + creditReport.getExclusion() + " - " + creditReport.getExclusionReason());
        } else if (creditReport.getLimitAmount().compareTo(amount) < 0) {
            logger.trace("Customer {} has insufficient credit limit: {}", customerId, creditReport.getLimitAmount());
            loanRequest.setStatus(LoanStatus.REJECTED);
            loanRequest.setRejectionReason("Insufficient credit limit");
        } else {
            loanRequest.setStatus(LoanStatus.APPROVED);
            logger.info("Loan request approved for customer: {}", customerId);
        }
        loanRequest.setLastUpdated(LocalDateTime.now());
        logger.info("Loan request created for customer: {}", customerId);
        return loanRequestRepository.save(loanRequest);
    }

    public List<LoanRequest> getLoanHistory(Long customerId) {
        logger.debug("Retrieving loan history for customer: {}", customerId);
        return loanRequestRepository.findByCustomerId(customerId);
    }
    public LoanRequest getLatestLoanRequest(Long customerId) {
        logger.debug("Retrieving latest loan request for customer: {}", customerId);
        return loanRequestRepository.findFirstByCustomerIdOrderByRequestDateDesc(customerId)
                .orElseThrow(() -> new NotFoundException("No loan requests found"));
    }

} 