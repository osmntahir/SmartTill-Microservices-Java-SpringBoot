package com.toyota.saleservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDto {
    private int id;

    private String name;
    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 100,  message = "Discount must be less than or equal to 100")
    private Long discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ProductDTO> products;


}
