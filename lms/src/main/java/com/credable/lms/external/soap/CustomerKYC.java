package com.credable.lms.external.soap;

import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import com.credable.lms.wsdl.customer.CustomerRequest;
import com.credable.lms.wsdl.customer.CustomerResponse;
import org.springframework.ws.client.WebServiceTransportException;
import org.springframework.ws.client.WebServiceIOException;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomerKYC {
    private static final Logger logger = LoggerFactory.getLogger(CustomerKYC.class);
    
    private final WebServiceTemplate kycWebServiceTemplate;

    public CustomerKYC(@Qualifier("kycWebServiceTemplate") WebServiceTemplate kycWebServiceTemplate) {
        this.kycWebServiceTemplate = kycWebServiceTemplate;
    }

    public CustomerResponse getCustomerKYCData(String customerNumber) {
        logger.debug("Retrieving KYC data for customer: {}", customerNumber);
        
        CustomerRequest request = new CustomerRequest();
        request.setCustomerNumber(customerNumber);
        
        try {
            CustomerResponse response = (CustomerResponse) kycWebServiceTemplate.marshalSendAndReceive(request);
            
            if (response == null) {
                logger.error("No response received from KYC service for customer: {}", customerNumber);
                throw new RuntimeException("No response received from KYC service");
            }
            
            logger.debug("Successfully retrieved KYC data for customer: {}", customerNumber);
            return response;
        } catch (WebServiceTransportException e) {
            
            logger.error("Failed to connect to KYC service for customer {}: {}", customerNumber, e.getMessage());
            throw new RuntimeException("Failed to connect to KYC service: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error querying KYC data for customer {}: {}", customerNumber, e.getMessage());
            throw new RuntimeException("Error querying KYC data: " + e.getMessage(), e);
        }
    }
}