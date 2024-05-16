package com.toyota.saleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CampaignDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal discount;
}
