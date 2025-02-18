# Inventory Service Microservice

The Inventory Service is a Spring Boot-based microservice designed to manage key components of an inventory system
including **Vendors**, **Products**, and **Purchase Orders**. It provides a RESTful API for creating, reading, updating,
and deleting these resources as well as performing specialized operations such as receiving purchase orders and managing
product quantities.

---

## Table of Contents

- [Features](#features)
- [Architecture Overview](#architecture-overview)
- [API Endpoints](#api-endpoints)
    - [Vendor Endpoints](#vendor-endpoints)
    - [Purchase Order Endpoints](#purchase-order-endpoints)
    - [Product Endpoints](#product-endpoints)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Service](#running-the-service)
- [Error Handling](#error-handling)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Vendor Management:** Create, update, retrieve, and delete vendor information.
- **Product Management:** CRUD operations for products, including quantity adjustments (add, deduct, sale).
- **Purchase Order Management:** Manage purchase orders including creation, updates, line item management, and receiving
  orders which updates associated product quantities.
- **Transactional Operations:** Uses transactions to ensure data consistency during inventory updates.
- **Optimistic Locking:** Handles concurrent updates with optimistic locking to prevent data conflicts.
- **RESTful API:** Clean and intuitive endpoints for interacting with the service.

---

## Architecture Overview

The microservice is organized into several packages:

- **Controllers:**
    - `VendorController`, `PurchaseOrderController`, and `ProductController` expose REST endpoints.

- **Services:**
    - `VendorService`, `PurchaseOrderService`, and `ProductService` encapsulate business logic and interact with
      repositories.

- **Repositories:**
    - Interfaces extending Spring Data JPA repositories to communicate with the underlying database.

- **Models:**
    - Entity classes for Vendor, Product, PurchaseOrder, and PurchaseOrderLineItem along with supporting enums (e.g.,
      `PurchaseOrderStatus`).

The service uses **Jakarta Persistence (JPA)** for ORM and **Spring Boot** for rapid application development.

---

## API Endpoints

### Vendor Endpoints

| Method | Endpoint          | Description                                  |
|--------|-------------------|----------------------------------------------|
| GET    | `/v1/vendor`      | Retrieve a list of all vendors.              |
| GET    | `/v1/vendor/{id}` | Retrieve details of a specific vendor by ID. |
| POST   | `/v1/vendor`      | Create a new vendor.                         |
| PATCH  | `/v1/vendor/{id}` | Update an existing vendor.                   |
| DELETE | `/v1/vendor/{id}` | Delete a vendor by ID.                       |

---

### Purchase Order Endpoints

| Method | Endpoint                      | Description                                                                        |
|--------|-------------------------------|------------------------------------------------------------------------------------|
| GET    | `/v1/purchase`                | Retrieve a list of all purchase orders.                                            |
| GET    | `/v1/purchase/{id}`           | Retrieve details of a specific purchase order by ID.                               |
| POST   | `/v1/purchase`                | Create a new purchase order.                                                       |
| PATCH  | `/v1/purchase/{id}`           | Update an existing purchase order (e.g., change order or delivery dates).          |
| PATCH  | `/v1/purchase/{id}/receive`   | Mark a purchase order as received. Updates product quantities based on line items. |
| POST   | `/v1/purchase/{id}/lineItems` | Add one or more line items to a purchase order.                                    |
| DELETE | `/v1/purchase/{id}`           | Delete a purchase order by ID.                                                     |

---

### Product Endpoints

| Method | Endpoint                        | Description                                                                              |
|--------|---------------------------------|------------------------------------------------------------------------------------------|
| GET    | `/v1/product`                   | Retrieve a list of all products.                                                         |
| GET    | `/v1/product/{id}`              | Retrieve details of a specific product by ID.                                            |
| POST   | `/v1/product`                   | Create a new product.                                                                    |
| PATCH  | `/v1/product/{id}`              | Update product details (name, description, price, quantity, etc.).                       |
| PATCH  | `/v1/product/{id}/add/{qty}`    | Add a specified quantity to a product’s inventory.                                       |
| PATCH  | `/v1/product/{id}/deduct/{qty}` | Deduct a specified quantity from a product’s inventory.                                  |
| PATCH  | `/v1/product/{id}/sale/{qty}`   | Record a sale by deducting the specified quantity from inventory (similar to deduction). |
| DELETE | `/v1/product/{id}`              | Delete a product by ID.                                                                  |

---

## Prerequisites

Before running the Inventory Service, ensure you have the following installed:

- **Java:** JDK 17 or higher
- **Build Tool:** Maven
- **Database:** PostgreSQL
- **IDE:** Optional (e.g., IntelliJ IDEA, Eclipse) for easier development and debugging

---

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/Matt-Hays/inventory-service.git
   cd inventory-service
   ```

2. **Build the Service:**

   ```bash
   mvn clean install
   ```

---

## Running the Service

1. **Run with Maven:**

   ```bash
   mvn spring-boot:run
   ```

2. **Run with Docker:**

   ```bash
   mvn clean spring-boot:build-image -DskipTests
   docker run -p 8080:8080 docker.io/library/inventory-service:0.0.1-SNAPSHOT
   ```

---

## Error Handling

The microservice handles errors using HTTP response codes:

- **404 Not Found** - Resource does not exist
- **400 Bad Request** - Invalid input data
- **422 Unprocessable Entity** - Optimistic locking issues
- **500 Internal Server Error** - General server errors
