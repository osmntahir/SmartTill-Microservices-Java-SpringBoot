package com.toyota.saleservice.dto;

import com.toyota.saleservice.domain.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Long id;
    private Long userId;
    private double totalPrice;
    private Date creationDate;
    private PaymentType paymentType;
}
