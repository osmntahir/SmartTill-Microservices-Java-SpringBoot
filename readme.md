# Toyota 32 Bit Project

## Introduction

The **Toyota 32 Bit Project** is a backend system designed using a microservice architecture. This system handles various services like user management, product listing, sales, and reporting. The project integrates **API Gateway** with **Keycloak** to manage authentication, authorization, and routing requests between the services.

The architecture ensures that each service has its responsibility, promoting scalability and maintainability. All services are deployed using Docker containers, and the API Gateway acts as a central point of communication between them.

## Technologies Used

- **JDK 17**
- **Spring Boot**
- **Spring Web & Spring Data**
- **Maven**
- **Git**
- **PostgreSQL**
- **Log4j2**
- **JUnit5**
- **Keycloak**
- **Docker**

## Architecture Overview

The project consists of multiple microservices, each responsible for specific operations. The API Gateway integrates with Keycloak for handling authentication and role-based access control (RBAC). It routes requests to the relevant microservices, ensuring secure communication between the components.

### Key Components

1. **API Gateway**:
    - Manages all routing between services.
    - Integrates with Keycloak for token-based authentication and authorization.
    - Handles role-based access control (RBAC) for all services, ensuring that only users with appropriate roles can access certain services.
    - **Base URL**: `http://localhost:8085`
    - Example request to get all products:
      - `GET http://localhost:8085/product/getAll`

2. **User Management Service**:
    - Handles CRUD operations for users.
    - Integrates with Keycloak to manage user roles such as CASHIER, MANAGER, and ADMIN.
    - Ensures that all changes made in the service are reflected in Keycloak.

3. **Product Service**:
    - Handles CRUD operations for products.
    - Provides product listings without requiring any specific role for access.
    - Implements pagination, filtering, and sorting for product data retrieval.

4. **Sale Service**:
    - Manages sales transactions, including products sold, total price, and applied campaigns.
    - Only **Cashiers** can create sales.
    - Ensures that the total price of a sale is updated when a product is removed from it.
    - Implements pagination, filtering, and sorting for sales data retrieval.

5. **Report Service**:
    - Generates reports for sales.
    - Allows store managers to export sales receipts as PDFs.

### Microservices and Docker Integration

Each service is containerized using Docker. The containers register themselves with the API Gateway, which routes requests accordingly. The API Gateway ensures that all authorization rules are enforced using Keycloak tokens.

### Deployment

The project is set up using **Docker Compose**. Each service has its container, and these containers communicate via the API Gateway. The database, PostgreSQL, is also containerized or can be set up externally based on your environment.

## How to Run the Project

To run the project locally, follow these steps:

1. **Clone the repository**:

    ```bash
    git clone https://github.com/osmntahir/Java-Toyota-32Bit-Backend.git
    ```

2. **Navigate to the project directory**:

    ```bash
    cd Java-Toyota-32Bit-Backend
    ```

3. **Build the project and create JAR files** (skip the tests during build):

    ```bash
    mvn clean install -DskipTests
    ```

4. **Run the Docker Compose setup**:

    ```bash
    docker-compose up --build
    ```

This will start all the services including the API Gateway, User Management Service, Product Service, Sale Service, Report Service, PostgreSQL, and Keycloak.

## Keycloak Integration

### OAuth 2.0 Authentication with Keycloak

The project uses Keycloak for OAuth 2.0 authentication. Tokens are generated through Keycloak and then passed to the API Gateway to authorize access to various services.

#### Step-by-step Token Generation via Postman:

1. Set the **Auth Type** to **OAuth 2.0**.
2. Choose the **Password Credentials** grant type.
3. Configure the token settings as follows (refer to the uploaded image for the configuration details):

    - **Access Token URL**: `http://keycloak:8080/realms/32bit_realm/protocol/openid-connect/token`
    - **Client ID**: `32bit_client`
    - **Client Secret**: `jLqPycRhcGsmRsicM7gEb7xgZ357GRrp`
    - **Username**: manager (for example)
    - **Password**: manager
    - **Scope**: `openid offline_access`

4. Click **Get New Access Token**. The token can now be used to access the endpoints.

### Initial User Setup with Keycloak

When Keycloak is initialized, users for all roles are automatically created. These default users can be used to test different roles in the system.

- **CASHIER Role**:  
  - Username: `cashier`  
  - Password: `cashier`

- **MANAGER Role**:  
  - Username: `manager`  
  - Password: `manager`

- **ADMIN Role**:  
  - Username: `admin`  
  - Password: `admin`

These users can access their respective endpoints and perform operations according to their roles.

### Accessing Keycloak Admin Console

To manage Keycloak through the admin console, go to:

http://keycloak:8080/admin/master/console/#/32bit_realm


## Core Functionalities

### **User Management Service**

Handles the management of users and their roles. This service connects to Keycloak to ensure that any changes are
reflected in Keycloak.

- **Roles**: CASHIER, MANAGER, ADMIN (each user must have at least one role).
- **Endpoints**:
    - `POST /user` - Add a new user with roles.
    - `PUT /user/{id}` - Update user details (only predefined roles like CASHIER, MANAGER, ADMIN can be updated).
    - `DELETE /user/{id}` - Soft delete a user (removes the user but keeps the record for future purposes).
    - `GET /user/{id}` - Retrieve user details.
    - `POST /assign-role/{id}` - Assign a role to a user.
    - `POST /unassign-role/{id}` - Remove a role from a user.

### **Product Service**

Manages the CRUD operations for products in the system.

- **Endpoints**:
    - `POST /product/add` - Create a new product.
    - `GET /product/getAll` - List all products.
    - `PUT /product/update/{id}` - Update a product's details.
    - `DELETE /product/delete/{id}` - Delete a product.

### **Sale Service**

This service handles all sales operations, including adding products to a sale, calculating total price, discounts, and
managing campaign-related functionalities.

#### **Sale Endpoints**

These endpoints are used for managing sales transactions:

- **Endpoints**:
    - `POST /sale/add` - Create a new sale (accessible by Cashiers).
    - `GET /sale/getAll` - Get a list of all sales with pagination, filtering, and sorting.
    - `GET /sale/get/{id}` - Retrieve a specific sale by ID.
    - `PUT /sale/update/{id}` - Update the details of a sale.
    - `DELETE /sale/delete/{id}` - Delete a sale by ID.

#### **Sold Product Endpoints**

These endpoints are used for managing the products sold in a particular sale:

- **Endpoints**:
    - `GET /sold-product/getAll` - Retrieve all sold products associated with sales.
    - `POST /sold-product/add/{id}/{saleId}` - Add a sold product (specified by product ID) to a sale (specified by sale
      ID).
    - `PUT /sold-product/update/{id}` - Update details of a sold product by ID.
    - `DELETE /sold-product/delete/{id}` - Remove a sold product from a sale by ID.

#### **Campaign Endpoints**

These endpoints manage sales campaigns and their relationships with specific products and sales:

- **Endpoints**:
    - `GET /campaign/getAll` - Retrieve a list of all available campaigns.
    - `POST /campaign/add` - Add a new campaign to the system.
    - `PUT /campaign/update/{id}` - Update details of a campaign by ID.
    - `DELETE /campaign/delete/{id}` - Remove a campaign by ID.

#### **Campaign Product Endpoints**

These endpoints manage the association of campaigns with products in sales:

- **Endpoints**:
    - `GET /campaign-product/getAll` - Retrieve all campaign-product associations.
    - `POST /campaign-product/add` - Add a new campaign-product association.
    - `PUT /campaign-product/update/{id}` - Update details of a campaign-product association by ID.
    - `DELETE /campaign-product/delete/{id}` - Remove a campaign-product association by ID.

### **Report Service**

Allows authorized users to generate and export sales reports in PDF format. Filtering and pagination are available for
large datasets.

- **Endpoints**:
    - `GET /report/sale/{id}` - Generate a PDF receipt for a specific sale.
    - `GET /report/sales` - Get a list of all sales with pagination, filtering, and sorting.

## Authorization & Role Management

All requests go through the **API Gateway**, which verifies the roles and permissions using **Keycloak**. Based on the role associated with the user, access to different services is granted or denied:

- **Admins** can manage users and their roles.
- **Cashiers** can handle sales but cannot view the list of sales.
- **Managers** can generate sales reports and also view the list of all sales.


## Sale Service Overview (Cash Register Logic)

In this system, a **cash register** logic is implemented where **Cashiers** create sales transactions and scan products into the sale. If there is an ongoing campaign for a product, the system automatically applies the discount during the sale. The sold products are added to the sale and necessary calculations such as total price, discount amount, and final price after discounts are automatically handled by the system.

### Process Flow:
1. **Creating a Sale**: 
   - A cashier first creates a new sale by sending a request to the `/sale/add` endpoint. 
   - The cashier's name is extracted from the JWT token to associate the sale with the cashier.

2. **Adding Products to the Sale**: 
   - Once the sale is created, the cashier starts scanning or adding products to the sale by calling the `/sold-product/add/{productId}/{saleId}` endpoint.
   - The system checks if the product is part of any active campaigns. If a campaign applies, the discount is automatically calculated and deducted from the product’s total price.

3. **Updating the Sale Total**: 
   - As products are added, the system recalculates the total sale price.
   - It also calculates the total discount amount and the final price after discounts for the entire sale.

4. **Finalizing the Sale**:
   - Once all products have been added to the sale, the total price, discount amount, and the final price after the discount are saved.
   - This completes the sales transaction, and the sale details are available for viewing or report generation.


## API Documentation

## Sale Service

This service manages sales, including creating, updating, deleting, and retrieving sale records.

### Create Sale

- **Endpoint**: `POST /sale/add`
- **Description**: Create a new sale.
- **Request**:
    ```json
    {
    "paymentType": "CREDIT_CARD",
    "soldProducts": []
    }
    ```
- **Response**:
    ```json
    {
    "id": 44,
    "date": "2024-09-06T20:47:29.1188235",
    "paymentType": "CREDIT_CARD",
    "totalPrice": 0.0,
    "totalDiscountAmount": 0.0,
    "totalDiscountedPrice": 0.0,
    "cashierName": "Osman Tahir OZDEMIR",
    "soldProducts": []
    }
    ```

### Get All Sales

- **Endpoint**: `GET /sale/getAll`
- **Description**: Retrieve a list of all sales with pagination, filtering, and sorting.
- **Response**:
    ```json
   {
    "content": [
        {
            "id": 45,
            "date": "2024-09-07T12:16:11.403047",
            "paymentType": "CREDIT_CARD",
            "totalPrice": 134.0,
            "totalDiscountAmount": 12.12,
            "totalDiscountedPrice": 121.88,
            "cashierName": "ali  veli",
            "soldProducts": [
                {
                    "id": 118,
                    "productId": 1,
                    "productName": "incir",
                    "price": 10.0,
                    "inventory": 0,
                    "discount": 12.0,
                    "discountAmount": 4.8,
                    "finalPriceAfterDiscount": 35.2,
                    "total": 35.2,
                    "quantity": 4,
                    "deleted": false
                },
                {
                    "id": 119,
                    "productId": 2,
                    "productName": "kiraz",
                    "price": 2.0,
                    "inventory": 0,
                    "discount": 12.0,
                    "discountAmount": 1.92,
                    "finalPriceAfterDiscount": 14.08,
                    "total": 14.08,
                    "quantity": 8,
                    "deleted": false
                },
                {
                    "id": 120,
                    "productId": 3,
                    "productName": "limon",
                    "price": 4.0,
                    "inventory": 0,
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 24.0,
                    "total": 24.0,
                    "quantity": 6,
                    "deleted": false
                },
                {
                    "id": 121,
                    "productId": 4,
                    "productName": "karpuz",
                    "price": 5.0,
                    "inventory": 0,
                    "discount": 12.0,
                    "discountAmount": 5.3999999999999995,
                    "finalPriceAfterDiscount": 39.6,
                    "total": 39.6,
                    "quantity": 9,
                    "deleted": false
                },
                {
                    "id": 122,
                    "productId": 5,
                    "productName": "visne",
                    "price": 9.0,
                    "inventory": 0,
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 9.0,
                    "total": 9.0,
                    "quantity": 1,
                    "deleted": false
                },
                {
                    "id": 118,
                    "productId": 1,
                    "productName": "incir",
                    "price": 10.0,
                    "inventory": 79,
                    "discount": 12.0,
                    "discountAmount": 4.8,
                    "finalPriceAfterDiscount": 35.2,
                    "total": 35.2,
                    "quantity": 4,
                    "deleted": false
                },
                {
                    "id": 119,
                    "productId": 2,
                    "productName": "kiraz",
                    "price": 2.0,
                    "inventory": 8,
                    "discount": 12.0,
                    "discountAmount": 1.92,
                    "finalPriceAfterDiscount": 14.08,
                    "total": 14.08,
                    "quantity": 8,
                    "deleted": false
                },
                {
                    "id": 120,
                    "productId": 3,
                    "productName": "limon",
                    "price": 4.0,
                    "inventory": 14,
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 24.0,
                    "total": 24.0,
                    "quantity": 6,
                    "deleted": false
                },
                {
                    "id": 121,
                    "productId": 4,
                    "productName": "karpuz",
                    "price": 5.0,
                    "inventory": 6,
                    "discount": 12.0,
                    "discountAmount": 5.3999999999999995,
                    "finalPriceAfterDiscount": 39.6,
                    "total": 39.6,
                    "quantity": 9,
                    "deleted": false
                },
                {
                    "id": 122,
                    "productId": 5,
                    "productName": "visne",
                    "price": 9.0,
                    "inventory": 19,
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 9.0,
                    "total": 9.0,
                    "quantity": 1,
                    "deleted": false
                }
            ]
        },
        {
            "id": 43,
            "date": "2024-09-06T16:07:24.160748",
            "paymentType": "CREDIT_CARD",
            "totalPrice": 155.0,
            "totalDiscountAmount": 6.6,
            "totalDiscountedPrice": 148.4,
            "cashierName": "Osman Tahir OZDEMIR",
            "soldProducts": [
                {
                    "id": 111,
                    "productId": 2,
                    "productName": "kiraz",
                    "price": 2.0,
                    "inventory": 0,
                    "discount": 12.0,
                    "discountAmount": 1.2,
                    "finalPriceAfterDiscount": 8.8,
                    "total": 8.8,
                    "quantity": 5,
                    "deleted": false
                },
                {
                    "id": 112,
                    "productId": 3,
                    "productName": "limon",
                    "price": 4.0,
                    "inventory": 0,
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 28.0,
                    "total": 28.0,
                    "quantity": 7,
                    "deleted": false
                },
                {
                    "id": 113,
                    "productId": 4,
                    "productName": "karpuz",
                    "price": 5.0,
                    "inventory": 0,
                    "discount": 12.0,
                    "discountAmount": 2.4,
                    "finalPriceAfterDiscount": 17.6,
                    "total": 17.6,
                    "quantity": 4,
                    "deleted": false
                },
                {
                    "id": 114,
                    "productId": 5,
                    "productName": "visne",
                    "price": 9.0,
                    "inventory": 0,
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 72.0,
                    "total": 72.0,
                    "quantity": 8,
                    "deleted": false
                },
                {
                    "id": 115,
                    "productId": 9,
                    "productName": "elma",
                    "price": 15.0,
                    "inventory": 0,
                    "discount": 12.0,
                    "discountAmount": 1.7999999999999998,
                    "finalPriceAfterDiscount": 13.2,
                    "total": 13.2,
                    "quantity": 1,
                    "deleted": false
                },
                {
                    "id": 110,
                    "productId": 1,
                    "productName": "incir",
                    "price": 10.0,
                    "inventory": 0,
                    "discount": 12.0,
                    "discountAmount": 1.2,
                    "finalPriceAfterDiscount": 8.8,
                    "total": 8.8,
                    "quantity": 1,
                    "deleted": false
                },
                {
                    "id": 111,
                    "productId": 2,
                    "productName": "kiraz",
                    "price": 2.0,
                    "inventory": 8,
                    "discount": 12.0,
                    "discountAmount": 1.2,
                    "finalPriceAfterDiscount": 8.8,
                    "total": 8.8,
                    "quantity": 5,
                    "deleted": false
                },
                {
                    "id": 112,
                    "productId": 3,
                    "productName": "limon",
                    "price": 4.0,
                    "inventory": 14,
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 28.0,
                    "total": 28.0,
                    "quantity": 7,
                    "deleted": false
                },
                {
                    "id": 113,
                    "productId": 4,
                    "productName": "karpuz",
                    "price": 5.0,
                    "inventory": 6,
                    "discount": 12.0,
                    "discountAmount": 2.4,
                    "finalPriceAfterDiscount": 17.6,
                    "total": 17.6,
                    "quantity": 4,
                    "deleted": false
                },
                {
                    "id": 114,
                    "productId": 5,
                    "productName": "visne",
                    "price": 9.0,
                    "inventory": 19,
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 72.0,
                    "total": 72.0,
                    "quantity": 8,
                    "deleted": false
                },
                {
                    "id": 115,
                    "productId": 9,
                    "productName": "elma",
                    "price": 15.0,
                    "inventory": 44,
                    "discount": 12.0,
                    "discountAmount": 1.7999999999999998,
                    "finalPriceAfterDiscount": 13.2,
                    "total": 13.2,
                    "quantity": 1,
                    "deleted": false
                },
                {
                    "id": 110,
                    "productId": 1,
                    "productName": "incir",
                    "price": 10.0,
                    "inventory": 79,
                    "discount": 12.0,
                    "discountAmount": 1.2,
                    "finalPriceAfterDiscount": 8.8,
                    "total": 8.8,
                    "quantity": 1,
                    "deleted": false
                }
            ]
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 5,
        "totalPages": 1,
        "totalElements": 2
    }
    }
    ```

### Get Sale by ID

- **Endpoint**: `GET /sale/get/{id}`
- **Description**: Retrieve a specific sale by its ID.
- **Response**:
    ```json
    {
    "id": 43,
    "date": "2024-09-06T16:07:24.160748",
    "paymentType": "CREDIT_CARD",
    "totalPrice": 155.0,
    "totalDiscountAmount": 6.6,
    "totalDiscountedPrice": 148.4,
    "cashierName": "Osman Tahir OZDEMIR",
    "soldProducts": [
        {
            "id": 111,
            "productId": 2,
            "productName": "kiraz",
            "price": 2.0,
            "inventory": 16,
            "discount": 12.0,
            "discountAmount": 1.2,
            "finalPriceAfterDiscount": 8.8,
            "total": 8.8,
            "quantity": 5,
            "deleted": false
        },
        {
            "id": 112,
            "productId": 3,
            "productName": "limon",
            "price": 4.0,
            "inventory": 20,
            "discount": 0.0,
            "discountAmount": 0.0,
            "finalPriceAfterDiscount": 28.0,
            "total": 28.0,
            "quantity": 7,
            "deleted": false
        },
        {
            "id": 113,
            "productId": 4,
            "productName": "karpuz",
            "price": 5.0,
            "inventory": 15,
            "discount": 12.0,
            "discountAmount": 2.4,
            "finalPriceAfterDiscount": 17.6,
            "total": 17.6,
            "quantity": 4,
            "deleted": false
        },
        {
            "id": 114,
            "productId": 5,
            "productName": "visne",
            "price": 9.0,
            "inventory": 20,
            "discount": 0.0,
            "discountAmount": 0.0,
            "finalPriceAfterDiscount": 72.0,
            "total": 72.0,
            "quantity": 8,
            "deleted": false
        },
        {
            "id": 115,
            "productId": 9,
            "productName": "elma",
            "price": 15.0,
            "inventory": 44,
            "discount": 12.0,
            "discountAmount": 1.7999999999999998,
            "finalPriceAfterDiscount": 13.2,
            "total": 13.2,
            "quantity": 1,
            "deleted": false
        },
        {
            "id": 110,
            "productId": 1,
            "productName": "incir",
            "price": 10.0,
            "inventory": 83,
            "discount": 12.0,
            "discountAmount": 1.2,
            "finalPriceAfterDiscount": 8.8,
            "total": 8.8,
            "quantity": 1,
            "deleted": false
        }
    ]
    }
    ```

### Update Sale

- **Endpoint**: `PUT /sale/update/{id}`
- **Description**: Update the details of a sale.
- **Request**:
    ```json
    {
    "paymentType": "DEBIT_CARD"
    }


    ```
- **Response**:
    ```json
    {
    "id": 42,
    "date": "2024-09-06T15:57:41.756801",
    "paymentType": "DEBIT_CARD",
    "totalPrice": 38.0,
    "totalDiscountAmount": 4.56,
    "totalDiscountedPrice": 33.44,
    "cashierName": "Osman Tahir ozdemir",
    "soldProducts": [
        {
            "id": 116,
            "productId": 4,
            "productName": "karpuz",
            "price": 5.0,
            "inventory": 0,
            "discount": 12.0,
            "discountAmount": 3.5999999999999996,
            "finalPriceAfterDiscount": 26.4,
            "total": 26.4,
            "quantity": 6,
            "deleted": false
        },
        {
            "id": 117,
            "productId": 2,
            "productName": "kiraz",
            "price": 2.0,
            "inventory": 0,
            "discount": 12.0,
            "discountAmount": 0.96,
            "finalPriceAfterDiscount": 7.04,
            "total": 7.04,
            "quantity": 4,
            "deleted": false
        }
         ]
      }
  
    ```

### Delete Sale

- **Endpoint**: `DELETE /sale/delete/{id}`
- **Description**: Delete a sale by its ID.
- **Response**:
    ```json
    {
      "message": "Sale with ID deleted successfully.",
      "status": "DELETED"
    }
    ```

## Sold Product Endpoints

### Get All Sold Products

- **Endpoint**: `GET /sold-product/getAll`
- **Description**: Retrieve all sold products.
- **Response**:
    ```json
    [
      
    ]
    ```

### Add Sold Product

- **Endpoint**: `POST /sold-product/add/{id}/{saleId}`
- **Description**: Add a product to a specific sale.
  - **Request**:
      ```json
      {
        "quantity": 4
      }
      ```
  - **Response**:
    ```json
    {

    "id": 117,
    "productId": 2,
    "productName": "kiraz",
    "price": 2.0,
    "inventory": 16,
    "discount": 12.0,
    "discountAmount": 0.96,
    "finalPriceAfterDiscount": 7.04,
    "total": 7.04,
    "quantity": 4,
    "deleted": false

    }
    ```

### Update Sold Product

- **Endpoint**: `PUT /sold-product/update/{id}`
- **Description**: Update the quantity of a sold product.
- **Request**:
    ```json
    {
      "quantity": 8
    }
    ```
- **Response**:
    ```json
    {
      "id": 117,
      "productId": 2,
      "productName": "kiraz",
      "price": 2.0,
      "inventory": 16,
      "discount": 12.0,
      "discountAmount": 1.92,
      "finalPriceAfterDiscount":14.08,
      "total": 16,
      "quantity": 8,
      "deleted": false
    }
    ```

### Delete Sold Product

- **Endpoint**: `DELETE /sold-product/delete/{id}`
- **Description**: Remove a sold product from a sale.
- **Response**:
    ```json
    {
      "message": "Sold product with ID deleted successfully.",
      "status": "DELETED"
    }
    ```

## Campaign Endpoints

### Get All Campaigns

- **Endpoint**: `GET /campaign/getAll`
- **Description**: Retrieve all campaigns.
- **Response**:
    ```json
   {
    "content": [
        {
            "id": 4,
            "name": "Winter sale",
            "description": "Up to 50% off on all summer items",
            "discount": 12,
            "campaignProducts": [
                {
                    "id": 4,
                    "productId": 1,
                    "deleted": false
                },
                {
                    "id": 5,
                    "productId": 2,
                    "deleted": false
                },
                {
                    "id": 8,
                    "productId": 4,
                    "deleted": false
                },
                {
                    "id": 7,
                    "productId": 9,
                    "deleted": false
                }
            ],
            "deleted": false
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "totalPages": 1,
        "totalElements": 1
      }
    }

    ```

### Add Campaign

- **Endpoint**: `POST /campaign/add`
- **Description**: Add a new campaign.
- **Request**:
    ```json
    {
      "name": "Holiday Discount",
      "discount": 20,
      "description": "20% off on all items during the holiday season."
    }
    ```
- **Response**:
    ```json
    {
    "id": 7,
    "name": "Holiday Discount",
    "description": "20% off on all items during the holiday season.",
    "discount": 20,
    "campaignProducts": null,
    "deleted": false
    }
    ```

### Update Campaign

- **Endpoint**: `PUT /campaign/update/{id}`
- **Description**: Update a specific campaign.
- **Request**:
    ```json
    {
      "name": "Holiday Discount Updated",
      "discount": 25,
      "description": "25% off on all items during the holiday season."
    }
    ```
- **Response**:
    ```json
    {
      "id": 7,
      "name": "Holiday Discount Updated",
      "description": "25% off on all items during the holiday season.",
      "discount": 25,
      "campaignProducts": [],
      "deleted": false
    }
    ```

### Delete Campaign

- **Endpoint**: `DELETE /campaign/delete/{id}`
- **Description**: Delete a campaign by ID.
- **Response**:
    ```json
    {
      "id": 7,
      "name": "Holiday Discount Updated",
      "description": "25% off on all items during the holiday season.",
      "discount": 25,
      "campaignProducts": [],
      "deleted": true
    }
    ```

## Campaign Product Endpoints

### Get All Campaign Products

- **Endpoint**: `GET /campaign-product/getAll`
- **Description**: Retrieve all campaign-product associations.
- **Response**:
    ```json
   [
     {
        "id": 4,
        "campaignId": 4,
        "productId": 1,
        "deleted": false
     },
     {
        "id": 5,
        "campaignId": 4,
        "productId": 2,
        "deleted": false
     },
     {
        "id": 8,
        "campaignId": 4,
        "productId": 4,
        "deleted": false
     },
     {
        "id": 7,
        "campaignId": 4,
        "productId": 9,
        "deleted": false
     }
   ]
    ```

### Add Campaign Product

- **Endpoint**: `POST /campaign-product/add`
- **Description**: Associate a campaign with a product.
- **Request**:
    ```json
    {
      "campaignId": 4,
      "productId": 4
    }
    ```
- **Response**:
    ```json
    {
    "id": 10,
    "campaignId": 4,
    "productId": 4,
    "deleted": false
    }   
    ```

### Update Campaign Product

- **Endpoint**: `PUT /campaign-product/update/{id}`
- **Description**: Update the campaign-product association.
- **Request**:
    ```json
    {
      "campaignId": 4,
      "productId": 9
    }
    ```
- **Response**:
    ```json
    {
      "id": 10,
      "campaignId": 4,
      "productId": 9
    }
    ```

### Delete Campaign Product

- **Endpoint**: `DELETE /campaign-product/delete/{id}`
- **Description**: Remove a campaign-product association.
- **Response**:
    ```json
    {
    "id": 10,
    "campaignId": 4,
    "productId": 9,
    "deleted": true

}

## Product Service

The **Product Service** manages all the product-related operations within the system. Below is a list of available endpoints and their usage.

### Get All Products
- **Endpoint**: `GET /product/getAll`
- **Description**: Retrieve a list of all products with pagination, filtering, and sorting.
- **Response**:
    ```json
    {
    "content": [
        {
            "id": 9,
            "name": "elma",
            "description": null,
            "price": 15.0,
            "inventory": 44,
            "active": true
        },
        {
            "id": 8,
            "name": "erik",
            "description": null,
            "price": 12.0,
            "inventory": 43,
            "active": true
        },
        {
            "id": 1,
            "name": "incir",
            "description": null,
            "price": 10.0,
            "inventory": 83,
            "active": true
        },
        {
            "id": 4,
            "name": "karpuz",
            "description": null,
            "price": 5.0,
            "inventory": 15,
            "active": true
        },
        {
            "id": 6,
            "name": "kayisi",
            "description": null,
            "price": 7.0,
            "inventory": 26,
            "active": true
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 5,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
    },
    "last": false,
    "totalPages": 2,
    "totalElements": 9,
    "first": true,
    "size": 5,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 5,
    "empty": false
    }
    ```
### Create Product
- **Endpoint**: `POST /product/add`
- **Description**: Create a new product.
- **Request**:
    ```json
    {
    "name": "muz",
    "description": "muz",
    "price": 10.0,
    "inventory": 10
    }
    ```
- **Response**: 
    ```json
    {
    "id": 10,
    "name": "muz",
    "description": "muz",
    "price": 10.0,
    "inventory": 10,
    "active": true
    }
    ```
### Update Product
- **Endpoint**: `PUT /product/update/{id}`
- **Description**: Update the details of a product.
- **Request**:
    ```json
    {
    "name": "Updated Product Namee",
    "price": 199.99,
    "inventory": 50
    }
    ```
- **Response**:
    ```json
    {
    "id": 11,
    "name": "Updated Product Namee",
    "description": null,
    "price": 199.99,
    "inventory": 50,
    "active": true
    }
    ```
### Delete Product
- **Endpoint**: `DELETE /product/delete/{id}`
- **Description**: Delete a product by its ID.
- **Response**:
    ```json
    {
      "status": 204,
      "message": "No content."
    }
    ```
## User Management Service

The **User Management Service** is responsible for managing users and their roles within the system. Below is a list of available endpoints and their usage.

### Get User
- **Endpoint**: `GET /user`
- **Description**: Retrieve a list of all users.
- **Response**:
    ```json
    [
    {
        "id": "1550629f-c91d-4ac3-8617-7747b503ccd2",
        "username": "superuser",
        "firstName": "osman tahir ",
        "lastName": "ozdemir",
        "email": "superuser@gmail.com",
        "password": null,
        "roles": [
            "default-roles-32bit-realm",
            "ADMIN"
        ]
    },
  {
        "id": "a23c6b13-481f-446b-a190-60bd26b862fe",
        "username": "user4",
        "firstName": "osman tahir ",
        "lastName": "ozdemir",
        "email": "user4@32bit.com",
        "password": null,
        "roles": [
            "default-roles-32bit-realm",
            "MANAGER"
        ]
    },
    {
        "id": "34eb3ff5-d56f-49f6-906c-464895cc1d4c",
        "username": "user6",
        "firstName": "UpdatedFirstName",
        "lastName": "UpdatedLastName",
        "email": "user6@example.com",
        "password": null,
        "roles": [
            "CASHIER",
            "default-roles-32bit-realm"
        ]
    }
    ]
    ```
### Create User
- **Endpoint**: `POST /user`
- **Description**: Create a new user with roles.
- **Request**:
    ```json
   {
   "firstName": "mahmut",
  "lastName": "mahmut",
  "email": "mahmut.ersoy@example.com",
  "username": "mahmut",
  "roles": ["MANAGER"],
  "password": "123"
   }
    ```
- **Response**:
    ```json
    {
    "message": "User created and roles assigned successfully",
  }
    ```
### Update User
- **Endpoint**: `PUT /user/{id}`
- **Description**: Update user details.
- **Request**:
    ```json
    {
    "firstName": "UpdatedFirstName",
  "lastName": "UpdatedLastName"
    }
    ```
- **Response**:
    ```json
    {
    "message": "User updated successfully"
    }
    ```
- **Request**:
    ```json
    {
    "roles": ["CASHIER"]
     }
    ```
- **Response**:
    ```json
    {
    "message": "User updated successfully with role adjustments."
    }
    ```
### Delete User
- **Endpoint**: `DELETE /user/{id}`
- **Description**: Soft delete a user by ID.
- **Response**:
    ```json
    {
    "message": "User disabled successfully",
    }
    ```
### Assign Role
- **Endpoint**: `POST /assign-role/{id}?roleName={roleName}`
- **Description**: Assign a role to a user.
- **Response**:
    ```json
    {
    "message": "Role assigned successfully"
    }
    ```
### Unassign Role
- **Endpoint**: `POST /unassign-role/{id}?roleName={roleName}`
- **Description**: Remove a role from a user.
- **Response**:
    ```json
    {
    "message": "Role unassigned successfully"
    }
    ```
## Report Service
- **Endpoint**: `GET /report/sale/{id}`
- **Description**: Generate a PDF receipt for a specific sale.
- **Response**: PDF file

  [sale_report_43.pdf](reports%2Fsale_report_43.pdf)

