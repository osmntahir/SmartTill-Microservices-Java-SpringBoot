package com.toyota.saleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignProductDto {
    private Long id;
    private Long campaignId;
    private Long productId;
}
