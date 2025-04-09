package com.credable.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import java.util.concurrent.TimeUnit;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContexts;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebServiceConfig {
    private static final Logger logger = LoggerFactory.getLogger(WebServiceConfig.class);

    @Value("${kyc.service.url}")
    private String kycServiceUrl;

    @Value("${kyc.service.username}")
    private String kycServiceUsername;

    @Value("${kyc.service.password}")
    private String kycServicePassword;

    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    @Value("${transaction.service.username}")
    private String transactionServiceUsername;  

    @Value("${transaction.service.password}")
    private String transactionServicePassword;

    @Bean
    public Jaxb2Marshaller getKycMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.credable.lms.wsdl.customer");
        
        // Set marshaller properties
        Map<String, Object> properties = new HashMap<>();
        properties.put("jaxb.formatted.output", true);
        marshaller.setMarshallerProperties(properties);
        
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller getTransactionMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.credable.lms.wsdl.transactions");
        
        // Set marshaller properties
        Map<String, Object> properties = new HashMap<>();
        properties.put("jaxb.formatted.output", true);
        marshaller.setMarshallerProperties(properties);
        
        return marshaller;
    }

    private WebServiceTemplate createWebServiceTemplate(String serviceUrl, String username, String password, Jaxb2Marshaller marshaller) {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);
        webServiceTemplate.setDefaultUri(serviceUrl);

        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
        messageSender.setCredentials(new UsernamePasswordCredentials(username, password));
        webServiceTemplate.setMessageSender(messageSender);

        logger.info("WebServiceTemplate configured with endpoint: {}", webServiceTemplate.getDefaultUri());
        return webServiceTemplate;
    }

    @Bean
    public WebServiceTemplate transactionWebServiceTemplate() {
        return createWebServiceTemplate(transactionServiceUrl, transactionServiceUsername, transactionServicePassword, getTransactionMarshaller());
    }

    @Bean
    public WebServiceTemplate kycWebServiceTemplate() {
        return createWebServiceTemplate(kycServiceUrl, kycServiceUsername, kycServicePassword, getKycMarshaller());
    }

} 