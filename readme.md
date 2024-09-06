# Java Toyota 32 Bit Project

## Introduction

The **Toyota 32 Bit Project** is a backend system designed using a microservice architecture. This system handles
various services like user management, product listing, sales, and reporting. The project integrates **API Gateway**
with **Keycloak** to manage authentication, authorization, and routing requests between the services.

The architecture ensures that each service has its responsibility, promoting scalability and maintainability. All
services are deployed using Docker containers, and the API Gateway acts as a central point of communication between
them.

### Technologies Used

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

The project consists of multiple microservices, each responsible for specific operations. The API Gateway integrates
with Keycloak for handling authentication and role-based access control (RBAC). It routes requests to the relevant
microservices, ensuring secure communication between the components.

### Key Components:

1. **API Gateway**:
    - Manages all routing between services.
    - Integrates with Keycloak for token-based authentication and authorization.
    - Handles role-based access control (RBAC) for all services, ensuring that only users with appropriate roles can
      access certain services.

2. **User Management Service**:
    - Handles CRUD operations for users.
    - Integrates with Keycloak to manage user roles such as CASHIER, MANAGER, and ADMIN.
    - Ensures that all changes made in the service are reflected in Keycloak.

3. **Product Service**:
    - Handles CRUD operations for products.
    - Provides product listings without requiring any specific role for access.
    - Implements pagination, filtering, and sorting for products data retrieval

4. **Sale Service**:
    - Manages sales transactions, including products sold, total price, and applied campaigns.
    - Role restrictions apply, allowing only **Cashiers** to create sales.
    - Ensures that the total price of a sale is updated when a product is removed from it.
    - Implements pagination, filtering, and sorting for sales data retrieval

5. **Report Service**:
    - Generates reports for sales.
    - Allows store managers to export sales receipts as PDFs.

### Microservices and Docker Integration

Each service is containerized using Docker. The containers register themselves with the API Gateway, which routes
requests accordingly. The API Gateway ensures that all authorization rules are enforced using Keycloak tokens.

### Deployment

The project is set up using **Docker Compose**. Each service has its container, and these containers communicate via the
API Gateway. The database, PostgreSQL, is also containerized or can be set up externally based on your environment.

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

## Authorization & Role Management

All requests go through the **API Gateway**, which verifies the roles and permissions using **Keycloak**. Based on the
role associated with the user, access to different services is granted or denied:

- **Admins** can manage users and their roles.
- **Cashiers** can handle sales.
- **Managers** can generate sales reports.

[//]: # ()

[//]: # (## How to Run Locally)

[//]: # ()

[//]: # (To run the project locally, ensure you have **Docker** installed. Follow the steps below to start the services:)

[//]: # ()

[//]: # (1. Clone the repository.)

[//]: # (   ```bash)

[//]: # (   git clone https://github.com/your-repository-url.git)

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
- **Description**: Retrieve a list of all sales.
- **Response**:
    ```json
    [
      {
  
      }
    ]
    ```

### Get Sale by ID

- **Endpoint**: `GET /sale/get/{id}`
- **Description**: Retrieve a specific sale by its ID.
- **Response**:
    ```json
    {
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

```

---
