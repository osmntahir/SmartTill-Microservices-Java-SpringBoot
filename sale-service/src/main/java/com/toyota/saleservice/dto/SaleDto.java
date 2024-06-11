package com.toyota.saleservice.dto;

import com.toyota.saleservice.domain.PaymentType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Long id;
    //private Long userId;
    private double totalPrice;
    @NotBlank(message = "Creation date must be not blank")
    private Date creationDate;
    @NotBlank(message = "Payment type must be not blank")
    private PaymentType paymentType;

    private List<SoldProductDto> soldProducts;
}
