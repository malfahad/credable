package com.credable.lms.util;

public class Constants {
    // API Endpoints
    public static final String CLIENT_REGISTRATION_URL = "https://scoringtest.credable.io/api/v1/client/createClient";
    
    // HTTP Headers
    public static final String CONTENT_TYPE_JSON = "application/json";
    
    // Error Messages
    public static final String ERROR_CLIENT_ALREADY_EXISTS = "Client already exists";
    public static final String ERROR_CLIENT_NOT_FOUND = "Client exists but not found";
    public static final String ERROR_REGISTRATION_FAILED = "Failed to register client";
    
    // Property Keys
    public static final String PROP_CLIENT_REGISTRATION_URL = "client.registration.url";
    public static final String PROP_CLIENT_NAME = "client.name";
    public static final String PROP_CLIENT_USERNAME = "client.username";
    public static final String PROP_CLIENT_PASSWORD = "client.password";
    
    // Request/Response Keys
    public static final String KEY_URL = "url";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN = "token";
    
    // Database Table Names
    public static final String TABLE_CLIENT_CONFIG = "client_config";
    
    // Column Names
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TOKEN = "token";
} 