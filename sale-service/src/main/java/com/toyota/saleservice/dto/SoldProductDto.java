package com.toyota.saleservice.dto;
import com.toyota.productservice.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SoldProductDto {
    private Long id;
    private ProductDto product;
    private double price;
    private int quantity;
    private double total;
}
