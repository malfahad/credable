# Loan Management System (LMS)

A Spring Boot-based Loan Management System that integrates with external services for credit scoring and customer verification.

## Demo API

A demo version of the API is available at http://143.244.171.99:8080/


## Features

- Customer registration and verification
- Credit score assessment
- Loan application processing
- Transaction history retrieval
- Integration with external KYC and transaction services via SOAP
- Integration with credit scoring service via REST

## Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- H2 Database (embedded)

## Project Structure

```
credable/
├── lms/                    # Main Loan Management System
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/credable/lms/
│   │   │   │       ├── config/         # Configuration classes
│   │   │   │       ├── controller/     # REST controllers
│   │   │   │       ├── domain/         # Entity classes
│   │   │   │       ├── dto/            # Data Transfer Objects
│   │   │   │       ├── external/       # External service integrations
│   │   │   │       ├── repository/     # JPA repositories
│   │   │   │       ├── service/        # Business logic
│   │   │   │       └── util/           # Utility classes
│   │   │   └── resources/
│   │   │       ├── wsdl/              # WSDL files
│   │   │       └── application.yml    # Application configuration
│   └── pom.xml
└── mockCRB/               # Mock Credit Reference Bureau
```

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd credable
   ```

2. Build and run using Docker Compose:
   ```bash
   docker-compose up --build
   ```

This will build and start:
- LMS service on port 8080
- Mock CBS service on port 8093  
- Mock Scoring service on port 8094
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Customer Management
- `POST /api/v1/customers/register` - Register a new customer
- `GET /api/v1/customers/{customerId}` - Get customer details

### Loan Management
- `POST /api/v1/loans/request` - Request a new loan
- `GET /api/v1/loans/{loanId}` - Get loan details
- `GET /api/v1/loans/customer/{customerId}` - Get customer's loans

### Transaction History
- `GET /api/v1/transactions/{customerId}` - Get customer's transaction history

## External Service Integration

### KYC Service
- SOAP-based integration
- WSDL: `customer.wsdl`
- Endpoint: `https://kycapitest.credable.io/service/customerWsdl.wsdl`

### Transaction Service
- SOAP-based integration
- WSDL: `transaction.wsdl`
- Endpoint: `https://trxapitest.credable.io/service/transactionWsdl.wsdl`

### Credit Scoring Service
- REST-based integration
- Endpoint: `https://scoringtest.credable.io/api/v1`

## Development

### Code Generation
The project uses JAXB to generate Java classes from WSDL files. To regenerate the classes:

```bash
mvn clean generate-sources
```

### Testing
Run the test suite:
```bash
mvn test
```

## Troubleshooting

### Common Issues

1. **WSDL Generation Issues**
   - Ensure the WSDL files are downloaded correctly
   - Check network connectivity to external services
   - Verify WSDL URLs are accessible

2. **Service Integration Issues**
   - Verify service endpoints are correct
   - Check network connectivity
   - Review service response logs

3. **Database Issues**
   - H2 database is embedded and starts automatically
   - Check application logs for database connection issues

## License

[Add your license information here]

## Contributing

[Add contribution guidelines here]

