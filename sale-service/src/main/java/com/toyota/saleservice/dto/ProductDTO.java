package com.toyota.saleservice.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private String description;
    private int inventory;

}
