package com.credable.lms.config;
import com.credable.lms.external.rest.ScoringAPIService;
import org.slf4j.Logger;    
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientRegistrationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ClientRegistrationConfig.class);

    @Value("${scoring.api.timeout:300000}")
    private int timeout;

    @Bean
    public RestTemplate restTemplate() {
        logger.debug("Configuring RestTemplate with timeout: {}ms (5 minutes)", timeout);
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return new RestTemplate(factory);
    }

    @Bean
    public CommandLineRunner registerClient(ScoringAPIService scoringAPIService) {
        return args -> {
            try {
                logger.info("Starting client registration process");
                scoringAPIService.registerClient();
                logger.info("Client registration completed successfully");
            } catch (Exception e) {
                logger.error("Failed to register client: {}", e.getMessage(), e);
            }
        };
    }
} 