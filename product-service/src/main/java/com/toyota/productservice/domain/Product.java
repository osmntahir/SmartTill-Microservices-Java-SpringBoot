package com.toyota.productservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data // Generates getters, setters, etc.
@Where(clause = "active = true") // Hibernate specific annotation
public class Product {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private int inventory;
    private boolean active = true; // Default value is true


    public Product(String s, double price) {
        this.name = s;
        this.price = price;
    }
    public Product(Long id, String name, double price, int inventory) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inventory = inventory;
    }
}
