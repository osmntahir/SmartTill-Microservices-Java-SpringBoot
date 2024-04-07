package com.toyota.productservice.domain;

import com.toyota.productservice.domain.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductTest {

    @Test
    void testProductConstructorAndGetters() {
        // Test data
        Long id = 1L;
        String name = "Test Product";
        String description = "Test Description";
        double price = 10.0;
        int inventory = 100;
        boolean active = true;

        // Create product
        Product product = new Product(id, name, description, price, inventory, active);

        // Check the values of the created product
        assertNotNull(product);
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(inventory, product.getInventory());
        assertEquals(active, product.isActive());
    }

    @Test
    void testProductSetters() {
        // Test data
        Long id = 1L;
        String name = "Test Product";
        String description = "Test Description";
        double price = 10.0;
        int inventory = 100;
        boolean active = true;

        // Create product
        Product product = new Product();

        // Set values using setters
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setInventory(inventory);
        product.setActive(active);

        // Check the set values
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(inventory, product.getInventory());
        assertEquals(active, product.isActive());
    }
    /**
     * Test for the constructor with name and price parameters.
     */
    @Test
    void testConstructorWithNameAndPrice() {
        // Test data
        String name = "Test Product";
        double price = 10.0;

        // Create product using constructor
        Product product = new Product(name, price);

        // Check the values of the created product
        assertNotNull(product);
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
    }

    /**
     * Test for the constructor with all parameters except description.
     */
    @Test
    void testConstructorWithAllParametersExceptDescription() {
        // Test data
        Long id = 1L;
        String name = "Test Product";
        double price = 10.0;
        int inventory = 100;

        // Create product using constructor
        Product product = new Product(id, name, price, inventory);

        // Check the values of the created product
        assertNotNull(product);
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
        assertEquals(inventory, product.getInventory());
        assertEquals(true, product.isActive()); // Check if active flag is true by default
    }
}