package com.credable.lms.controller;

import com.credable.lms.domain.LoanRequest;
import com.credable.lms.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/{customerId}/loan")
    public ResponseEntity<LoanRequest> requestLoan(
            @PathVariable Long customerId,
            @RequestParam BigDecimal amount) {
        LoanRequest loanRequest = loanService.requestLoan(customerId, amount);
        return ResponseEntity.ok(loanRequest);
    }

    @GetMapping("/{customerId}/loans")
    public ResponseEntity<List<LoanRequest>> getLoanHistory(@PathVariable Long customerId) {
        List<LoanRequest> loanHistory = loanService.getLoanHistory(customerId);
        return ResponseEntity.ok(loanHistory);
    }

    @GetMapping("/{customerId}/loan/latest")
    public ResponseEntity<LoanRequest> getLatestLoanRequest(@PathVariable Long customerId) {
        LoanRequest latestLoan = loanService.getLatestLoanRequest(customerId);
        return ResponseEntity.ok(latestLoan);
    }
} 