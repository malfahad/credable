package com.credable.lms.controller;

import com.credable.lms.external.soap.TransactionData;
import com.credable.lms.wsdl.transactions.TransactionsResponse; 
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionData transactionData;

    public TransactionsController(TransactionData transactionData) {
        this.transactionData = transactionData;
    }

    /**
     * Retrieves transaction history for a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return TransactionResponse containing the customer's transaction history.
     */
    @GetMapping("/{customerId}")
    public TransactionsResponse getCustomerTransactions(@PathVariable String customerId) {
        return transactionData.getCustomerTransactions(customerId);
    }
} 