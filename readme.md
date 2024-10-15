# Cash Register 

## Introduction

The **Cash Register ** is a backend system designed using a microservice architecture. This system handles
various services like user management, product listing, sales, and reporting. The project integrates **API Gateway**
with **Keycloak** to manage authentication, authorization, and routing requests between the services.

The architecture ensures that each service has its responsibility, promoting scalability and maintainability. All
services are deployed using Docker containers, and the API Gateway acts as a central point of communication between
them.

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

The project consists of multiple microservices, each responsible for specific operations. The API Gateway integrates
with Keycloak for handling authentication and role-based access control (RBAC). It routes requests to the relevant
microservices, ensuring secure communication between the components.

### Key Components

1. **API Gateway**:
    - Manages all routing between services.
    - Integrates with Keycloak for token-based authentication and authorization.
    - Handles role-based access control (RBAC) for all services, ensuring that only users with appropriate roles can
      access certain services.
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

Each service is containerized using Docker. The containers register themselves with the API Gateway, which routes
requests accordingly. The API Gateway ensures that all authorization rules are enforced using Keycloak tokens.

### Deployment

The project is set up using **Docker Compose**. Each service has its container, and these containers communicate via the
API Gateway. The database, PostgreSQL, is also containerized or can be set up externally based on your environment.

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

This will start all the services including the API Gateway, User Management Service, Product Service, Sale Service,
Report Service, PostgreSQL, and Keycloak.

## Keycloak Integration

### OAuth 2.0 Authentication with Keycloak

The project uses Keycloak for OAuth 2.0 authentication. Tokens are generated through Keycloak and then passed to the API
Gateway to authorize access to various services.

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

When Keycloak is initialized, users for all roles are automatically created. These default users can be used to test
different roles in the system.

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

- **Username**: `admin`
- **Password**: `admin`

[http://keycloak:8080/admin/master/console/#/32bit_realm](http://keycloak:8080/admin/master/console/#/32bit_realm)


## Core Functionalities

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
    - `POST /campaign/addProducts/{campaignId}` - Add products to a campaign.
    - `DELETE /campaign/removeProducts/{campaignId}` - Remove products from a campaign.
    - `DELETE /campaign/removeAllProducts/{campaignId}` - Remove all products from a campaign.

### **Report Service**

Allows authorized users to generate and export sales reports in PDF format. Filtering and pagination are available for
large datasets.

- **Endpoints**:
    - `GET /report/sale/{id}` - Generate a PDF receipt for a specific sale.
    - `GET /report/all-sales` - Generate a PDF receipt for all sales
    - `GET /report/sales` - Get a list of all sales with pagination, filtering, and sorting.

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


## Authorization & Role Management

All requests go through the **API Gateway**, which verifies the roles and permissions using **Keycloak**. Based on the
role associated with the user, access to different services is granted or denied:

- **Admins** can manage users and their roles.
- **Cashiers** can handle sales but cannot view the list of sales.
- **Managers** can generate sales reports and also view the list of all sales.

## Sale Service Overview (Cash Register Logic)

In this system, a **cash register** logic is implemented where **Cashiers** create sales transactions and scan products
into the sale. If there is an ongoing campaign for a product, the system automatically applies the discount during the
sale. The sold products are added to the sale and necessary calculations such as total price, discount amount, and final
price after discounts are automatically handled by the system.

### Process Flow:

1. **Creating a Sale**:
    - A cashier first creates a new sale by sending a request to the `/sale/add` endpoint.
    - The cashier's name is extracted from the JWT token to associate the sale with the cashier.

2. **Adding Products to the Sale**:
    - Once the sale is created, the cashier starts scanning or adding products to the sale by calling
      the `/sold-product/add/{saleId}/{productId}` endpoint.
    - The system checks if the product is part of any active campaigns. If a campaign applies, the discount is
      automatically calculated and deducted from the product’s total price.

3. **Updating the Sale Total**:
    - As products are added, the system recalculates the total sale price.
    - It also calculates the total discount amount and the final price after discounts for the entire sale.

4. **Finalizing the Sale**:
    - Once all products have been added to the sale, the total price, discount amount, and the final price after the
      discount are saved.
    - This completes the sales transaction, and the sale details are available for viewing or report generation.

## API Documentation

## Product Service

The **Product Service** manages all the product-related operations within the system. Below is a list of available
endpoints and their usage.

### Get All Products

- **Endpoint**: `GET /product/getAll`
- **Description**: Retrieve a list of all products with pagination, filtering, and sorting.

  #### Request Parameters:

| Parameter         | Type    | Required | Default Value      | Description                                                           |
|-------------------|---------|----------|--------------------|-----------------------------------------------------------------------|
| **page**          | integer | No       | `0`                | The page number to retrieve.                                          |
| **size**          | integer | No       | `5`                | The number of items per page.                                         |
| **name**          | string  | No       | `""`               | Filter products by name. Returns products containing this string.     |
| **minPrice**      | double  | No       | `0`                | Filter products with a minimum price.                                 |
| **maxPrice**      | double  | No       | `Double.MAX_VALUE` | Filter products with a maximum price.                                 |
| **sortBy**        | string  | No       | `"name"`           | Field to sort the results by.                                         |
| **sortDirection** | string  | No       | `"ASC"`            | The direction to sort the results. Can be either `"ASC"` or `"DESC"`. |

- **Response**:
    ```json
    {
    "content": [
        {
            "id": 4,
            "name": "apricot",
            "description": "apricot",
            "price": 5.0,
            "inventory": 100,
            "active": true
        },
        {
            "id": 1,
            "name": "banana",
            "description": "banana",
            "price": 4.0,
            "inventory": 80,
            "active": true
        },
        {
            "id": 2,
            "name": "cherry",
            "description": "cherry",
            "price": 2.0,
            "inventory": 96,
            "active": true
        },
        {
            "id": 3,
            "name": "watermelon",
            "description": "watermelon",
            "price": 6.0,
            "inventory": 92,
            "active": true
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 5,
        "sort": {
            "sorted": true,
            "empty": false,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 4,
    "last": true,
    "size": 5,
    "number": 0,
    "sort": {
        "sorted": true,
        "empty": false,
        "unsorted": false
    },
    "numberOfElements": 4,
    "first": true,
    "empty": false
    }
    ```

### Create Product

- **Endpoint**: `POST /product/add`
- **Description**: Create a new product.
- **Request**:
    ```json
    {
    "name": "banana",
    "description": "banana",
    "price": 10.0,
    "inventory": 10
    }
    ```
- **Response**:
    ```json
    {
    "id": 1,
    "name": "banana",
    "description": "banana",
    "price": 4.0,
    "inventory": 100,
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
    "id": 8,
    "date": "2024-10-08T10:39:18.3470285",
    "paymentType": "CREDIT_CARD",
    "totalPrice": 0.0,
    "totalDiscountAmount": 0.0,
    "totalDiscountedPrice": 0.0,
    "cashierName": "Osman  Tahir  Ozdemir",
    "soldProducts": []
    }
    ```

### Get All Sales

- **Endpoint**: `GET /sale/getAll`
- **Description**: Retrieve a list of all sales with pagination, filtering, and sorting.

#### Request Parameters:

| Parameter                   | Type         | Required | Default Value         | Description                                                                                              |
|-----------------------------|--------------|----------|-----------------------|----------------------------------------------------------------------------------------------------------|
| **page**                    | integer      | No       | `0`                   | The page number to retrieve.                                                                             |
| **size**                    | integer      | No       | `5`                   | The number of items per page.                                                                            |
| **minTotalPrice**           | double       | No       | `0.0`                 | Minimum total price for the sale.                                                                        |
| **maxTotalPrice**           | double       | No       | `Double.MAX_VALUE`    | Maximum total price for the sale.                                                                        |
| **minTotalDiscountAmount**  | double       | No       | `0.0`                 | Minimum total discount amount for the sale.                                                              |
| **maxTotalDiscountAmount**  | double       | No       | `Double.MAX_VALUE`    | Maximum total discount amount for the sale.                                                              |
| **minTotalDiscountedPrice** | double       | No       | `0.0`                 | Minimum total discounted price for the sale.                                                             |
| **maxTotalDiscountedPrice** | double       | No       | `Double.MAX_VALUE`    | Maximum total discounted price for the sale.                                                             |
| **startDate**               | datetime     | No       | `2023-01-01T00:00:00` | Start date for filtering sales (format: `YYYY-MM-DDTHH:MM:SS`).                                          |
| **endDate**                 | datetime     | No       | `Now`                 | End date for filtering sales (format: `YYYY-MM-DDTHH:MM:SS`).                                            |
| **paymentType**             | string       | No       | `""`                  | Filter by payment type (e.g., `CASH`, `CREDIT_CARD`, `DEBIT_CARD`).                                      |
| **cashierName**             | string       | No       | `""`                  | Filter by cashier's name.                                                                                |
| **deleted**                 | boolean      | No       | `false`               | Include deleted sales if `true`.                                                                         |
| **sortBy**                  | List<string> | No       | `"name"`              | Field(s) to sort the results by. Example values could be `"totalPrice"`, `"date"`, `"cashierName"`, etc. |
| **sortOrder**               | string       | No       | `"ASC"`               | The direction to sort the results. Can be `"ASC"` for ascending or `"DESC"` for descending order.        |

- **Response**:
    ```json
   {
    "content": [
        {
            "id": 1,
            "date": "2024-10-15T07:49:52.556131",
            "paymentType": "CREDIT_CARD",
            "totalPrice": 144.0,
            "totalDiscountAmount": 21.599999999999998,
            "totalDiscountedPrice": 122.4,
            "cashierName": "Osman  Tahir  Ozdemir",
            "soldProducts": [
                {
                    "id": 1,
                    "product": {
                        "id": 1,
                        "name": "banana",
                        "price": 4.0,
                        "description": "banana",
                        "inventory": 88,
                        "active": true
                    },
                    "discount": 30.0,
                    "discountAmount": 14.399999999999999,
                    "finalPriceAfterDiscount": 33.6,
                    "total": 48.0,
                    "campaignName": "Weekend Offer",
                    "quantity": 12,
                    "deleted": false
                },
                {
                    "id": 2,
                    "product": {
                        "id": 2,
                        "name": "cherry",
                        "price": 2.0,
                        "description": "cherry",
                        "inventory": 96,
                        "active": true
                    },
                    "discount": 30.0,
                    "discountAmount": 2.4,
                    "finalPriceAfterDiscount": 5.6,
                    "total": 8.0,
                    "campaignName": "Weekend Offer",
                    "quantity": 4,
                    "deleted": false
                },
                {
                    "id": 3,
                    "product": {
                        "id": 3,
                        "name": "watermelon",
                        "price": 6.0,
                        "description": "watermelon",
                        "inventory": 77,
                        "active": true
                    },
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 48.0,
                    "total": 48.0,
                    "campaignName": null,
                    "quantity": 8,
                    "deleted": false
                },
                {
                    "id": 4,
                    "product": {
                        "id": 4,
                        "name": "apricot",
                        "price": 5.0,
                        "description": "apricot",
                        "inventory": 92,
                        "active": true
                    },
                    "discount": 12.0,
                    "discountAmount": 4.8,
                    "finalPriceAfterDiscount": 35.2,
                    "total": 40.0,
                    "campaignName": "Limited Time Offer",
                    "quantity": 8,
                    "deleted": false
                }
            ]
        },
        {
            "id": 2,
            "date": "2024-10-15T07:51:21.620015",
            "paymentType": "DEBIT_CARD",
            "totalPrice": 90.0,
            "totalDiscountAmount": 0.0,
            "totalDiscountedPrice": 90.0,
            "cashierName": "Osman  Tahir  Ozdemir",
            "soldProducts": [
                {
                    "id": 5,
                    "product": {
                        "id": 3,
                        "name": "watermelon",
                        "price": 6.0,
                        "description": "watermelon",
                        "inventory": 77,
                        "active": true
                    },
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 90.0,
                    "total": 90.0,
                    "campaignName": null,
                    "quantity": 15,
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
    "id": 1,
    "date": "2024-10-15T07:49:52.556131",
    "paymentType": "CREDIT_CARD",
    "totalPrice": 144.0,
    "totalDiscountAmount": 21.599999999999998,
    "totalDiscountedPrice": 122.4,
    "cashierName": "Osman  Tahir  Ozdemir",
    "soldProducts": [
        {
            "id": 1,
            "product": {
                "id": 1,
                "name": "banana",
                "price": 4.0,
                "description": "banana",
                "inventory": 88,
                "active": true
            },
            "discount": 30.0,
            "discountAmount": 14.399999999999999,
            "finalPriceAfterDiscount": 33.6,
            "total": 48.0,
            "campaignName": "Weekend Offer",
            "quantity": 12,
            "deleted": false
        },
        {
            "id": 2,
            "product": {
                "id": 2,
                "name": "cherry",
                "price": 2.0,
                "description": "cherry",
                "inventory": 96,
                "active": true
            },
            "discount": 30.0,
            "discountAmount": 2.4,
            "finalPriceAfterDiscount": 5.6,
            "total": 8.0,
            "campaignName": "Weekend Offer",
            "quantity": 4,
            "deleted": false
        },
        {
            "id": 3,
            "product": {
                "id": 3,
                "name": "watermelon",
                "price": 6.0,
                "description": "watermelon",
                "inventory": 77,
                "active": true
            },
            "discount": 0.0,
            "discountAmount": 0.0,
            "finalPriceAfterDiscount": 48.0,
            "total": 48.0,
            "campaignName": null,
            "quantity": 8,
            "deleted": false
        },
        {
            "id": 4,
            "product": {
                "id": 4,
                "name": "apricot",
                "price": 5.0,
                "description": "apricot",
                "inventory": 92,
                "active": true
            },
            "discount": 12.0,
            "discountAmount": 4.8,
            "finalPriceAfterDiscount": 35.2,
            "total": 40.0,
            "campaignName": "Limited Time Offer",
            "quantity": 8,
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
    "id": 1,
    "date": "2024-10-15T07:49:52.556131",
    "paymentType": "DEBIT_CARD",
    "totalPrice": 144.0,
    "totalDiscountAmount": 21.599999999999998,
    "totalDiscountedPrice": 122.4,
    "cashierName": "Osman  Tahir  Ozdemir",
    "soldProducts": [
        {
            "id": 1,
            "product": {
                "id": 1,
                "name": "banana",
                "price": 4.0,
                "description": "banana",
                "inventory": 88,
                "active": true
            },
            "discount": 30.0,
            "discountAmount": 14.399999999999999,
            "finalPriceAfterDiscount": 33.6,
            "total": 48.0,
            "campaignName": "Weekend Offer",
            "quantity": 12,
            "deleted": false
        },
        {
            "id": 2,
            "product": {
                "id": 2,
                "name": "cherry",
                "price": 2.0,
                "description": "cherry",
                "inventory": 96,
                "active": true
            },
            "discount": 30.0,
            "discountAmount": 2.4,
            "finalPriceAfterDiscount": 5.6,
            "total": 8.0,
            "campaignName": "Weekend Offer",
            "quantity": 4,
            "deleted": false
        },
        {
            "id": 3,
            "product": {
                "id": 3,
                "name": "watermelon",
                "price": 6.0,
                "description": "watermelon",
                "inventory": 77,
                "active": true
            },
            "discount": 0.0,
            "discountAmount": 0.0,
            "finalPriceAfterDiscount": 48.0,
            "total": 48.0,
            "campaignName": null,
            "quantity": 8,
            "deleted": false
        },
        {
            "id": 4,
            "product": {
                "id": 4,
                "name": "apricot",
                "price": 5.0,
                "description": "apricot",
                "inventory": 92,
                "active": true
            },
            "discount": 12.0,
            "discountAmount": 4.8,
            "finalPriceAfterDiscount": 35.2,
            "total": 40.0,
            "campaignName": "Limited Time Offer",
            "quantity": 8,
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
-

#### Request Parameters:

| Parameter                      | Type    | Required | Default Value       | Description                                                                                       |
|--------------------------------|---------|----------|---------------------|---------------------------------------------------------------------------------------------------|
| **page**                       | integer | No       | `0`                 | The page number to retrieve.                                                                      |
| **size**                       | integer | No       | `5`                 | The number of items per page.                                                                     |
| **name**                       | string  | No       | `""`                | Filter sold products by name. Returns sold products containing this string.                       |
| **minPrice**                   | double  | No       | `0.0`               | Minimum price for sold products.                                                                  |
| **maxPrice**                   | double  | No       | `Double.MAX_VALUE`  | Maximum price for sold products.                                                                  |
| **minQuantity**                | integer | No       | `0`                 | Minimum quantity for sold products.                                                               |
| **maxQuantity**                | integer | No       | `Integer.MAX_VALUE` | Maximum quantity for sold products.                                                               |
| **minDiscountPercentage**      | double  | No       | `0.0`               | Minimum discount percentage for sold products.                                                    |
| **maxDiscountPercentage**      | double  | No       | `Double.MAX_VALUE`  | Maximum discount percentage for sold products.                                                    |
| **minDiscountAmount**          | double  | No       | `0.0`               | Minimum discount amount for sold products.                                                        |
| **maxDiscountAmount**          | double  | No       | `Double.MAX_VALUE`  | Maximum discount amount for sold products.                                                        |
| **minFinalPriceAfterDiscount** | double  | No       | `0.0`               | Minimum final price after discount for sold products.                                             |
| **maxFinalPriceAfterDiscount** | double  | No       | `Double.MAX_VALUE`  | Maximum final price after discount for sold products.                                             |
| **minTotalPrice**              | double  | No       | `0.0`               | Minimum total price for sold products.                                                            |
| **maxTotalPrice**              | double  | No       | `Double.MAX_VALUE`  | Maximum total price for sold products.                                                            |
| **deleted**                    | boolean | No       | `false`             | Include deleted sold products if `true`.                                                          |
| **sortBy**                     | string  | No       | `"name"`            | Field to sort the results by. Example values could be `"name"`, `"price"`, `"quantity"`, etc.     |
| **sortDirection**              | string  | No       | `"ASC"`             | The direction to sort the results. Can be `"ASC"` for ascending or `"DESC"` for descending order. |

- **Response**:
    ```json
     {
    "content": [
        {
            "id": 4,
            "product": {
                "id": 4,
                "name": "apricot",
                "price": 5.0,
                "description": "apricot",
                "inventory": 92,
                "active": true
            },
            "discount": 12.0,
            "discountAmount": 4.8,
            "finalPriceAfterDiscount": 35.2,
            "total": 40.0,
            "campaignName": "Limited Time Offer",
            "quantity": 8,
            "deleted": false
        },
        {
            "id": 1,
            "product": {
                "id": 1,
                "name": "banana",
                "price": 4.0,
                "description": "banana",
                "inventory": 88,
                "active": true
            },
            "discount": 30.0,
            "discountAmount": 14.399999999999999,
            "finalPriceAfterDiscount": 33.6,
            "total": 48.0,
            "campaignName": "Weekend Offer",
            "quantity": 12,
            "deleted": false
        },
        {
            "id": 2,
            "product": {
                "id": 2,
                "name": "cherry",
                "price": 2.0,
                "description": "cherry",
                "inventory": 96,
                "active": true
            },
            "discount": 30.0,
            "discountAmount": 2.4,
            "finalPriceAfterDiscount": 5.6,
            "total": 8.0,
            "campaignName": "Weekend Offer",
            "quantity": 4,
            "deleted": false
        },
        {
            "id": 3,
            "product": {
                "id": 3,
                "name": "watermelon",
                "price": 6.0,
                "description": "watermelon",
                "inventory": 92,
                "active": false
            },
            "discount": 0.0,
            "discountAmount": 0.0,
            "finalPriceAfterDiscount": 48.0,
            "total": 48.0,
            "campaignName": null,
            "quantity": 8,
            "deleted": false
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 5,
        "totalPages": 1,
        "totalElements": 4
    }
    }
    ```

### Add Sold Product

- **Endpoint**: `POST /sold-product/add/{saleId}/{productId}`
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
        "id": 7,
        "product": {
            "id": 1,
            "name": "banana",
            "price": 4.0,
            "description": "banana",
            "inventory": 84,
            "active": true
        },
        "discount": 30.0,
        "discountAmount": 4.8,
        "finalPriceAfterDiscount": 11.2,
        "total": 16.0,
        "campaignName": "Weekend Offer",
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
    "id": 7,
    "product": {
        "id": 1,
        "name": "banana",
        "price": 4.0,
        "description": "banana",
        "inventory": 80,
        "active": true
    },
    "discount": 30.0,
    "discountAmount": 9.6,
    "finalPriceAfterDiscount": 22.4,
    "total": 32.0,
    "campaignName": "Weekend Offer",
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

#### Request Parameters:

| Parameter                 | Type            | Required | Default Value | Description                                                                                              |
|---------------------------|-----------------|----------|---------------|----------------------------------------------------------------------------------------------------------|
| **page**                  | integer         | No       | `0`           | The page number to retrieve.                                                                             |
| **size**                  | integer         | No       | `10`          | The number of items per page.                                                                            |
| **name**                  | string          | No       | `""`          | Filter campaigns by name. Returns campaigns containing this string in their name.                        |
| **minDiscountPercentage** | double          | No       | `0`           | Filter campaigns with a minimum discount percentage.                                                     |
| **maxDiscountPercentage** | double          | No       | `100`         | Filter campaigns with a maximum discount percentage.                                                     |
| **deleted**               | boolean         | No       | `false`       | Include deleted campaigns in the results if `true`.                                                      |
| **sortBy**                | List of strings | No       | `[]`          | Fields to sort the results by. Accepts multiple field names.                                             |
| **sortDirection**         | string          | No       | `"ASC"`       | The direction to sort the results. Can be either `"ASC"` for ascending or `"DESC"` for descending order. |

- **Response**:
    ```json
    {
        "content": [
            {
                "id": 1,
                "name": "Weekend Offer",
                "discountPercentage": 30,
                "products": [
                    {
                        "id": 2,
                        "name": "cherry",
                        "description": "cherry",
                        "price": 2.0,
                        "inventory": 96,
                        "active": true
                    },
                    {
                        "id": 1,
                        "name": "banana",
                        "description": "banana",
                        "price": 4.0,
                        "inventory": 80,
                        "active": true
                    }
                ]
            },
            {
                "id": 2,
                "name": "Limited Time Offer",
                "discountPercentage": 12,
                "products": [
                    {
                        "id": 4,
                        "name": "apricot",
                        "description": "apricot",
                        "price": 5.0,
                        "inventory": 100,
                        "active": false
                    }
                ]
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 10,
            "totalPages": 1,
            "totalElements": 2
        }
    }
    ```

### Add Campaign

- **Endpoint**: `POST /campaign/add`
- **Description**: Add a new campaign.
- **Request**:
    ```json
   {
    "name": "Weekend Offer",
    "discountPercentage": 30
    }

    ```
- **Response**:
    ```json
  {
    "id": 1,
    "name": "Weekend Offer",
    "discountPercentage": 30,
    "products": null
    }
    ```

### Update Campaign

- **Endpoint**: `PUT /campaign/update/{id}`
- **Description**: Update a specific campaign.
- **Request**:
    ```json
  {
   
    "discountPercentage": 30
    }
    ```
- **Response**:
    ```json
    {
    "id": 1,
    "name": "Weekend Offer",
    "discountPercentage": 30,
    "products": null
    }
    ```

### Delete Campaign

- **Endpoint**: `DELETE /campaign/delete/{id}`
- **Description**: Delete a campaign by ID.
- **Response**:
    ```json
  {
      "message": "Campaign with ID deleted successfully.",
      "status": "DELETED"
    }
    ```
  ### Add Products to Campaign

- **Endpoint**: `POST /campaign/addProducts/{campaignId}`
- **Description**: Add products to an existing campaign.
- **Request**:
    ```json
   [1,2]
    ```
- **Response**:
    ```json
   {
    "id": 1,
    "name": "Weekend Offer",
    "discountPercentage": 30,
    "products": [
        {
            "id": 2,
            "name": "cherry",
            "description": "cherry",
            "price": 2.0,
            "inventory": 96,
            "active": true
        },
        {
            "id": 1,
            "name": "banana",
            "description": "banana",
            "price": 4.0,
            "inventory": 80,
            "active": true
        }
    ]
    }
    ```

### Remove Products from Campaign

- **Endpoint**: `DELETE /campaign/removeProducts/{campaignId}`
- **Description**: Remove specific products from a campaign.
- **Request**:
    ```json
  [1]
    ```
- **Response**:
    ```json
     {
        "id": 1,
        "name": "Weekend Offer",
        "discountPercentage": 30,
        "products": [
            {
                "id": 2,
                "name": "cherry",
                "description": "cherry",
                "price": 2.0,
                "inventory": 96,
                "active": true
            }
        ]
    }
    ```

### Remove All Products from Campaign

- **Endpoint**: `DELETE /campaign/removeAllProducts/{campaignId}`
- **Description**: Remove all products from a campaign.
- **Response**:
    ```json
   {
    "id": 1,
    "name": "Weekend Offer",
    "discountPercentage": 30,
    "products": null
    }
    ```

## Report Service

### Get sale by id to generate

- **Endpoint**: `GET /report/sale/{id}`
- **Description**: Generate a PDF receipt for a specific sale.
- **Response**: PDF file as a byte array

  [sale_report_1.pdf](reports%2Fsale_report_1.pdf)

### Get all sales to generate

- **Endpoint**: `GET /report/all-sales`
- **Description**: Generate a PDF file containing details of all sales.
- **Response**: PDF file as a byte array, listing all sales with their respective details such as sale ID, cashier name, sold products, total price, and discounts.

  [all_sales_report.pdf](reports%2Fall_sales_report.pdf)

### Get sales report

- **Endpoint**: `GET /report/sales`
- **Description**: Retrieve a sales report with optional filtering, sorting, and pagination options.

#### Request Parameters:

| Parameter                   | Type         | Required | Default Value         | Description                                                                                              |
|-----------------------------|--------------|----------|-----------------------|----------------------------------------------------------------------------------------------------------|
| **page**                    | integer      | No       | `0`                   | The page number to retrieve.                                                                             |
| **size**                    | integer      | No       | `5`                   | The number of items per page.                                                                            |
| **minTotalPrice**           | double       | No       | `0.0`                 | Minimum total price for the sale.                                                                        |
| **maxTotalPrice**           | double       | No       | `Double.MAX_VALUE`    | Maximum total price for the sale.                                                                        |
| **minTotalDiscountAmount**  | double       | No       | `0.0`                 | Minimum total discount amount for the sale.                                                              |
| **maxTotalDiscountAmount**  | double       | No       | `Double.MAX_VALUE`    | Maximum total discount amount for the sale.                                                              |
| **minTotalDiscountedPrice** | double       | No       | `0.0`                 | Minimum total discounted price for the sale.                                                             |
| **maxTotalDiscountedPrice** | double       | No       | `Double.MAX_VALUE`    | Maximum total discounted price for the sale.                                                             |
| **startDate**               | datetime     | No       | `2023-01-01T00:00:00` | Start date for filtering sales (format: `YYYY-MM-DDTHH:MM:SS`).                                          |
| **endDate**                 | datetime     | No       | `Now`                 | End date for filtering sales (format: `YYYY-MM-DDTHH:MM:SS`).                                            |
| **paymentType**             | string       | No       | `""`                  | Filter by payment type (e.g., `CASH`, `CREDIT_CARD`, `DEBIT_CARD`).                                      |
| **cashierName**             | string       | No       | `""`                  | Filter by cashier's name.                                                                                |
| **deleted**                 | boolean      | No       | `false`               | Include deleted sales if `true`.                                                                         |
| **sortBy**                  | List<string> | No       | `"name"`              | Field(s) to sort the results by. Example values could be `"totalPrice"`, `"date"`, `"cashierName"`, etc. |
| **sortOrder**               | string       | No       | `"ASC"`               | The direction to sort the results. Can be `"ASC"` for ascending or `"DESC"` for descending order.        |

#### Response:

```json
{
    "content": [
        {
            "id": 1,
            "date": "2024-10-15T07:49:52.556131",
            "paymentType": "DEBIT_CARD",
            "totalPrice": 104.0,
            "totalDiscountAmount": 16.799999999999997,
            "totalDiscountedPrice": 87.2,
            "cashierName": "Osman  Tahir  Ozdemir",
            "soldProducts": [
                {
                    "id": 1,
                    "product": {
                        "id": 1,
                        "name": "banana",
                        "price": 4.0,
                        "inventory": 80,
                        "active": true
                    },
                    "discount": 30.0,
                    "discountAmount": 14.399999999999999,
                    "finalPriceAfterDiscount": 33.6,
                    "total": 48.0,
                    "campaignName": "Weekend Offer",
                    "quantity": 12,
                    "deleted": false
                },
                {
                    "id": 2,
                    "product": {
                        "id": 2,
                        "name": "cherry",
                        "price": 2.0,
                        "inventory": 96,
                        "active": true
                    },
                    "discount": 30.0,
                    "discountAmount": 2.4,
                    "finalPriceAfterDiscount": 5.6,
                    "total": 8.0,
                    "campaignName": "Weekend Offer",
                    "quantity": 4,
                    "deleted": false
                },
                {
                    "id": 3,
                    "product": {
                        "id": 3,
                        "name": "watermelon",
                        "price": 6.0,
                        "inventory": 92,
                        "active": false
                    },
                    "discount": 0.0,
                    "discountAmount": 0.0,
                    "finalPriceAfterDiscount": 48.0,
                    "total": 48.0,
                    "campaignName": null,
                    "quantity": 8,
                    "deleted": false
                }
            ]
        },
        {
            "id": 2,
            "date": "2024-10-15T07:51:21.620015",
            "paymentType": "DEBIT_CARD",
            "totalPrice": 32.0,
            "totalDiscountAmount": 9.6,
            "totalDiscountedPrice": 22.4,
            "cashierName": "Osman  Tahir  Ozdemir",
            "soldProducts": [
                {
                    "id": 7,
                    "product": {
                        "id": 1,
                        "name": "banana",
                        "price": 4.0,
                        "inventory": 80,
                        "active": true
                    },
                    "discount": 30.0,
                    "discountAmount": 9.6,
                    "finalPriceAfterDiscount": 22.4,
                    "total": 32.0,
                    "campaignName": "Weekend Offer",
                    "quantity": 8,
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

## User Management Service

The **User Management Service** is responsible for managing users and their roles within the system. Below is a list of
available endpoints and their usage.

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
    "message": "User created and roles assigned successfully"
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
    "message": "User disabled successfully"
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

