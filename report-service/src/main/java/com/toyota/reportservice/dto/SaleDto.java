package com.toyota.reportservice.dto;

import jakarta.validation.Valid;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    //private Long userId;
    private Long id;
    @NotNull(message = "Creation date must be not null")
    private LocalDateTime date;
    @NotNull(message = "Total price must be not null")
    private PaymentType paymentType;
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0.0")
    private double totalPrice = 0.0;
    private double totalDiscountAmount;
    private double totalDiscountedPrice;
    private String cashierName;
    @Valid
    private List<SoldProductDto> soldProducts;
}
