package com.toyota.saleservice.dto;

import com.toyota.saleservice.domain.PaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    //private Long userId;
    private Long id;
    @NotNull(message = "Creation date must be not null")
    private Date creationDate;
    @NotNull(message = "Total price must be not null")
    private PaymentType paymentType;
    @Valid
    private List<SoldProductDto> soldProducts;
}
