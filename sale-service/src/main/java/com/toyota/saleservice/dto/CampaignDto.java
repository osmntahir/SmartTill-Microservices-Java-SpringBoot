package com.toyota.saleservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CampaignDto {
    private Long id;
    @NotBlank(message = "Name must be not blank")
    private String name;
    private String description;
    @NotBlank(message = "Discount must be not blank")
    @Min(value = 0, message = "Discount must be greater than 0")
    @Max(value = 100,  message = "Discount must be less than 101")
    private long discount;
}
