package com.toyota.saleservice.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldProductDto {
    private Long id;
//    private Long productId;
//    private String productName;
//    private double price;
//    private int inventory;
    private ProductDTO product;

    private double discount;
    private double discountAmount;
    private double finalPriceAfterDiscount;
    private double total;

    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private int quantity;

    private boolean deleted;
}
