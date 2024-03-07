package com.toyota.productservice.dto;


import lombok.Data;

// Structure mirrors Product.java but NO database annotations
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
   // private String sku;
    private int inventory;
}
