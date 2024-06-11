package com.toyota.saleservice.dto;
import com.toyota.productservice.dto.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SoldProductDto {
    private Long id;
    private ProductDto productDto;
    @NotBlank(message = "Price must be not blank")
    private double price;
    @NotBlank(message = "Quantity must be not blank")
    private int quantity;

    private double total;
}
