# Customer and Product Management API

This Spring Boot application provides API endpoints to manage Customers and Products.

## Prerequisites

- JDK 17 or later
- Your favorite IDE (IntelliJ IDEA Preferred)
- MSQL Database 
- Postman Client


## How to Run:

```bash
   git clone https://github.com/mohkhalidabdulaziz/customerproduct-api.git

   cd customerproduct-api
   
   spring-boot:run   
 ````


## Project Structure

The project follows a standard Maven structure:

- `src/main/java`: Contains the main application code.
- `src/main/resources`: Contains application properties, logback configuration, and database setup.
- `src/test`: Contains unit tests.

## Database Setup

This application uses MySQL as the database. You can modify the database configuration in `src/main/resources/application.yml`.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dev-customerproduct
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect

```

## Swagger API Documentation
Swagger is integrated for API documentation and testing. Access the Swagger UI using the following URL: http://localhost:8080/customerproduct-api/swagger-ui/index.html





## Logging Configuration
Logging is configured to output DEBUG-level logs
for the com.customerproduct.customerproductapi package. 
Log files are stored in the directory specified by the logging.file.path property. Update the logging configuration in src/main/resources/logback.xml as needed.

<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <!-- Console Appender -->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- File Appender -->
    <appender name="file" class="ch.qos.logback.core.FileAppender">
        <file>${logging.file.path}/${spring.application.name}.log</file>
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Application Log Levels -->
    <logger name="com.customerproduct.customerproductapi" level="${logging.level.com.customerproduct.customerproductapi}"/>

    <!-- Root Logger -->
    <root level="info">
        <appender-ref ref="console"/>
        <appender-ref ref="file"/>
    </root>

</configuration>


## Unit Tests with Mockito and JUnit
Unit tests for the Customer and Product APIs are implemented using Mockito and JUnit. These tests are located in the src/test directory.

## Test Cases

### Customer Service Unit Tests

#### 1. Create Customer

**Scenario**: When a valid customer DTO is provided, it should create a new customer.

**Steps**:
1. Invoke the `createCustomer` method with a valid `CustomerDTO`.
2. Verify that the returned `CustomerDTO` contains the correct information.
3. Check that the customer is successfully persisted in the database.

#### 2. Get All Customers

**Scenario**: Retrieving a list of all customers.

**Steps**:
1. Invoke the `getAllCustomers` method.
2. Verify that a non-empty list of customers is returned.
3. Check that the returned list contains the expected customers.

#### 3. Get Customer by ID

**Scenario**: Retrieving a customer by their ID.

**Steps**:
1. Create a customer in the database.
2. Invoke the `getCustomerById` method with the created customer's ID.
3. Verify that the returned `CustomerDTO` matches the expected customer.

#### 4. Update Customer

**Scenario**: Updating an existing customer.

**Steps**:
1. Create a customer in the database.
2. Modify some fields in the customer DTO.
3. Invoke the `updateCustomer` method with the modified DTO and customer ID.
4. Verify that the returned `CustomerDTO` reflects the updates.
5. Check that the customer in the database is updated accordingly.

#### 5. Delete Customer

**Scenario**: Deleting an existing customer.

**Steps**:
1. Create a customer in the database.
2. Invoke the `deleteCustomer` method with the created customer's ID.
3. Verify that the customer is no longer present in the database.

### Product Service Unit Tests

#### 1. Create Product

**Scenario**: When a valid product DTO is provided, it should create a new product.

**Steps**:
1. Invoke the `createProduct` method with a valid `ProductDTO`.
2. Verify that the returned `ProductDTO` contains the correct information.
3. Check that the product is successfully persisted in the database.

#### 2. Get All Products

**Scenario**: Retrieving a list of all products.

**Steps**:
1. Invoke the `getAllProducts` method.
2. Verify that a non-empty list of products is returned.
3. Check that the returned list contains the expected products.

#### 3. Get Product by ID

**Scenario**: Retrieving a product by its ID.

**Steps**:
1. Create a product in the database.
2. Invoke the `getProductById` method with the created product's ID.
3. Verify that the returned `ProductDTO` matches the expected product.

#### 4. Update Product

**Scenario**: Updating an existing product.

**Steps**:
1. Create a product in the database.
2. Modify some fields in the product DTO.
3. Invoke the `updateProduct` method with the modified DTO and product ID.
4. Verify that the returned `ProductDTO` reflects the updates.
5. Check that the product in the database is updated accordingly.

#### 5. Delete Product

**Scenario**: Deleting an existing product.

**Steps**:
1. Create a product in the database.
2. Invoke the `deleteProduct` method with the created product's ID.
3. Verify that the product is no longer present in the database.



