package com.credable.lms.external.soap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.credable.lms.wsdl.transactions.TransactionsRequest;
import com.credable.lms.wsdl.transactions.TransactionsResponse;

@Service
public class TransactionData {
    private static final Logger logger = LoggerFactory.getLogger(TransactionData.class);
    
    private final WebServiceTemplate webServiceTemplate;
    private final String transactionsEndpoint = "https://trxapitest.credable.io/service/transactionWsdl.wsdl";

    public TransactionData(@Qualifier("transactionWebServiceTemplate") WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public TransactionsResponse getCustomerTransactions(String customerId) {
        logger.debug("Retrieving transactions for customer: {}", customerId);
        
        // Create request object based on WSDL schema
        TransactionsRequest request = new TransactionsRequest();
        request.setCustomerNumber(customerId);

        try {
            // Make SOAP call and get response
            TransactionsResponse response = (TransactionsResponse) webServiceTemplate.marshalSendAndReceive(request);
            
            if (response == null) {
                logger.error("No response received from transaction service for customer: {}", customerId);
                throw new RuntimeException("No response received from transaction service");
            }
            
            logger.debug("Successfully retrieved transactions for customer: {}", customerId);
            return response;
        } catch (Exception e) {
            logger.error("Error querying transaction data for customer {}: {}", customerId, e.getMessage());
            throw new RuntimeException("Error querying transaction data: " + e.getMessage(), e);
        }
    }

} 