package com.toyota.saleservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldProductDto {
    private Long id;

    @Valid
    private ProductDTO productDto;


    private double discount;
    private double total;


    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private int quantity;

    private boolean deleted;
}
