package com.toyota.saleservice.dto;

import com.toyota.saleservice.domain.CampaignProduct;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDto {
    private Long id;

    @NotBlank(message = "Name must be not blank")
    private String name;

    private String description;

    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 100,  message = "Discount must be less than or equal to 100")
    private long discount;

    private List<CampaignProduct> campaignProducts;

    private boolean deleted = Boolean.FALSE;
}
