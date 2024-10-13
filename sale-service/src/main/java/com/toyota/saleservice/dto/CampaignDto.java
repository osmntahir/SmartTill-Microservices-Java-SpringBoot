package com.toyota.saleservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDto {
    private Long id;

    private String name;

    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 100,  message = "Discount must be less than or equal to 100")
    private Long discountPercentage;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//    private LocalDate startDate;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//    private LocalDate endDate;

    private List<ProductDTO> products;
}