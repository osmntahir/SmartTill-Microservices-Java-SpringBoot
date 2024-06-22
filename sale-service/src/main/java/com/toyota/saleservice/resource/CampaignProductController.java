package com.toyota.saleservice.resource;


import com.toyota.saleservice.domain.CampaignProduct;
import com.toyota.saleservice.dto.CampaignProductDto;
import com.toyota.saleservice.service.abstracts.CampaignProductService;
import com.toyota.saleservice.service.common.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign-products")
@RequiredArgsConstructor
public class CampaignProductController {

    @Autowired
    private final CampaignProductService campaignProductService;

    @GetMapping("/getAll")
    public ResponseEntity<List<CampaignProductDto>> getAllCampaignProducts() {
        List<CampaignProductDto> campaignProducts = campaignProductService.getAllCampaignProducts();
        if (campaignProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(campaignProducts);
    }


    @PostMapping("/add")
    public ResponseEntity <CampaignProductDto> addCampaignProduct(
            @RequestBody CampaignProductDto campaignProductDto)
    {
        CampaignProductDto response = campaignProductService.addCampaignProduct(campaignProductDto);

        if(response == null)
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CampaignProductDto> updateCampaignProduct(
            @PathVariable Long id,
            @RequestBody CampaignProductDto campaignProductDto)
    {
        CampaignProductDto response = campaignProductService.updateCampaignProduct(id, campaignProductDto);

        if(response == null)
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity <CampaignProductDto> deleteCampaignProduct(@PathVariable Long id)
    {
        CampaignProductDto response = campaignProductService.deleteCampaignProduct(id);

        if(response == null)
        {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
