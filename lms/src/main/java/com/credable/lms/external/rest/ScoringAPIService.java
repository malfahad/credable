package com.credable.lms.external.rest;

import com.credable.lms.domain.ClientConfig;
import com.credable.lms.domain.CreditReport;
import com.credable.lms.repository.ClientConfigRepository;
import com.credable.lms.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScoringAPIService {
    private static final Logger logger = LoggerFactory.getLogger(ScoringAPIService.class);
    private static final Logger traceLogger = LoggerFactory.getLogger("TRACE");
    
    private final ClientConfigRepository clientConfigRepository;
    private final RestTemplate restTemplate;

    @Value("${client.registration.url}")
    private String registrationUrl;

    @Value("${client.name}")
    private String clientName;

    @Value("${client.username}")
    private String username;

    @Value("${client.password}")
    private String password;

    @Value("${lms.url}")
    private String lmsUrl;

    @Value("${scoring.api.base.url}")
    private String scoringApiBaseUrl;

    public ScoringAPIService(ClientConfigRepository clientConfigRepository, RestTemplate restTemplate) {
        this.clientConfigRepository = clientConfigRepository;
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.setBearerAuth(token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ClientConfig getClientConfig() {
        return clientConfigRepository.findAll()
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Client not registered"));
    }

    private <T> ResponseEntity<T> executeWithRetry(String url, HttpMethod method, HttpEntity<?> request, 
            ParameterizedTypeReference<T> responseType, int maxRetries) throws InterruptedException {
        Exception lastException = null;
        
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                return restTemplate.exchange(url, method, request, responseType);
            } catch (Exception e) {
                lastException = e;
                logger.warn("Attempt {} failed: {}", attempt + 1, e.getMessage());
                if (attempt < maxRetries - 1) {
                    Thread.sleep(1000 * (attempt + 1)); 
                }
            }
        }
        
        throw new RuntimeException("Request failed after " + maxRetries + " attempts", lastException);
    }

    @Transactional
    public void registerClient() {
        logger.info("Starting client registration process");
        traceLogger.trace("Registration URL: {}", registrationUrl);

        try {
            if (clientConfigRepository.count() > 0) {
                logger.info("Client is already registered");
                return;
            }

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("url", lmsUrl);
            requestBody.put("name", clientName);
            requestBody.put("username", username);
            requestBody.put("password", password);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, createAuthHeaders(null));
            
            logger.debug("Sending registration request to: {}", registrationUrl);
            traceLogger.trace("Request body: {}", requestBody);

            ResponseEntity<Map> response = restTemplate.exchange(
                registrationUrl,
                HttpMethod.POST,
                request,
                Map.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new RuntimeException("Registration failed: Invalid response");
            }

            Map<String, Object> responseBody = response.getBody();
            String token = (String) responseBody.get("token");
            String url = (String) responseBody.get("url");

            if (token == null || url == null) {
                throw new RuntimeException("Invalid registration response: Missing token or URL");
            }

            ClientConfig clientConfig = new ClientConfig();
            clientConfig.setName(clientName);
            clientConfig.setUrl(url);
            clientConfig.setToken(token);

            clientConfigRepository.save(clientConfig);
            logger.info("Client registration completed successfully");

        } catch (Exception e) {
            logger.error("Error during client registration: {}", e.getMessage());
            throw new RuntimeException("Failed to register client: " + e.getMessage(), e);
        }
    }

    public String initiateCreditScoreRequest(Long customerId) {
        logger.debug("Initiating credit score request for customer: {}", customerId);
        try {
            ClientConfig config = getClientConfig();
            HttpEntity<?> request = new HttpEntity<>(createAuthHeaders(config.getToken()));

            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                scoringApiBaseUrl + "/initiateQueryScore/" + customerId,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Map<String, String>>() {}
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new RuntimeException("Failed to initiate credit score request");
            }

            String scoreToken = response.getBody().get("token");
            if (scoreToken == null) {
                throw new RuntimeException("No score token received in response");
            }

            logger.info("Successfully initiated credit score request for customer: {}", customerId);
            return scoreToken;

        } catch (Exception e) {
            logger.error("Error initiating credit score request for customer {}: {}", customerId, e.getMessage());
            throw new RuntimeException("Failed to initiate credit score request: " + e.getMessage());
        }
    }

    public CreditReport queryCreditScore(Long customerId, String scoreToken) {
        logger.debug("Querying credit score for customer: {}", customerId);
        try {
            ClientConfig config = getClientConfig();
            HttpEntity<?> request = new HttpEntity<>(createAuthHeaders(config.getToken()));

            ResponseEntity<Map<String, String>> response = executeWithRetry(
                scoringApiBaseUrl + "/queryScore/" + scoreToken,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Map<String, String>>() {},
                3
            );

            Map<String, String> responseBody = response.getBody();
            logger.debug("Received credit report response: {}", responseBody);

            if (responseBody.get("score") == null || responseBody.get("limitAmount") == null) {
                throw new RuntimeException("Invalid credit report response format");
            }

            int creditScore = Integer.parseInt(responseBody.get("score"));
            BigDecimal limitAmount = new BigDecimal(responseBody.get("limitAmount"));
            String exclusion = responseBody.getOrDefault("exclusion", "No Exclusion");
            String exclusionReason = responseBody.getOrDefault("exclusionReason", "No Exclusion");

            logger.trace("Credit report received for customer {}: score={}, limit={}, exclusion={}", 
                customerId, creditScore, limitAmount, exclusion);
            return new CreditReport(creditScore, limitAmount, exclusion, exclusionReason);

        } catch (Exception e) {
            logger.error("Error initiating credit report request for customer {}: {}", customerId, e.getMessage());
            throw new RuntimeException("Failed to initiate credit report request: " + e.getMessage());
        }
    }
}
