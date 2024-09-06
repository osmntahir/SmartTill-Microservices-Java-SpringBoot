# Toyota 32Bit Backend Project

This project is a microservices-based backend application for a retail system. It manages user authentication and authorization, product inventory, sales, reports, and more, using a set of distinct services. The application is designed with scalability, maintainability, and security in mind, following SOLID principles and best practices in modern software development.

## Project Architecture

The system follows a layered architecture, dividing responsibilities into distinct modules:

- **Domain Layer**: Contains the core business entities.
- **Service Layer**: Implements the business logic and interacts with the DAO and DTO layers.
- **DAO (Data Access Object) Layer**: Interacts with the database.
- **DTO (Data Transfer Object) Layer**: Facilitates communication between services and external clients.

### Microservices

- **API Gateway**: Handles authentication, authorization, and routing requests to relevant microservices.
- **User Management Service**: Manages user accounts and roles, integrated with Keycloak for role-based access control.
- **Product Service**: Manages product data.
- **Sale Service**: Manages sales transactions and integrates with products and campaigns.
- **Report Service**: Generates sales reports, including PDF receipts.
- **Discovery Server**: Registers services for easy routing via the API Gateway.

## Technologies Used

- **Java**: JDK 17
- **Framework**: Spring Boot (Spring Web, Spring Data)
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Logging**: Log4j2
- **Testing**: JUnit 5
- **Version Control**: Git
- **Authentication/Authorization**: Keycloak, JWT
- **Containerization**: Docker

## Key Features

### User Management
- Manages users and their roles (Admin, Manager, Cashier).
- Integrated with **Keycloak** for handling user authentication and authorization.
- **Soft delete** for user removal.

#### 1. Get All Users

- **Endpoint**: `GET /user`
- **Description**: Retrieves all users from the system.
- **Response**:
   - `200 OK`: Returns a list of users in the system.
   - `204 No Content`: No users found in the system.
- **Example**:

  ```bash
  GET /user

### Sales Service
- Records sales transactions, products, and applied campaigns.
- Calculates discounts and generates total sale amounts.
- Integrated with JWT tokens to extract cashier information from API requests.
- Supports sales report generation in PDF format via the Report Service.

### Report Service
- Generates detailed sales reports.
- Allows generating receipts for specific sales in **PDF format**.

### API Gateway
- Serves as a centralized entry point for all services.
- Ensures secure access to services via **token-based authentication** and **role-based access control**.

### Product Service
- Handles the creation, updating, and deletion of product data.
- Retrieves a list of products.

## Setup and Installation

### Prerequisites

- JDK 17+
- Maven
- PostgreSQL
- Docker (for containerization)
- Keycloak (for user management)

### Steps to Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/osmntahir/Java-Toyota-32Bit-Backend.git
   cd toyota-32bit-backend


