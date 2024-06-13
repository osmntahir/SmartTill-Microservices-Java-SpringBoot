package com.toyota.saleservice.dto;

import com.toyota.productservice.dto.ProductDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldProductDto {
    private Long id;

    @NotNull(message = "Product must be provided")
    private ProductDto productDto;

    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private int quantity;
}
