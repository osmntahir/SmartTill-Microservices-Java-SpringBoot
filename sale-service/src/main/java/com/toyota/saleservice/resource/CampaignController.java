package com.toyota.saleservice.resource;


import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.service.abstracts.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign")
@RequiredArgsConstructor
public class CampaignController {

    @Autowired
    private final CampaignService campaignService;

    @GetMapping("/getAll")
    public PaginationResponse <CampaignDto> getAllCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") Double minDiscountPercentage,
            @RequestParam(required = false, defaultValue = 100 + "") Double maxDiscountPercentage,
            @RequestParam(defaultValue = "false") boolean deleted,
            @RequestParam(defaultValue = "") List<String> sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return campaignService.getCampaignsFiltered(page, size, name, minDiscountPercentage, maxDiscountPercentage, deleted, sortBy, sortDirection);
    }

    @PostMapping("/add")
    public ResponseEntity<CampaignDto>addCampaign (@RequestBody  CampaignDto campaignDto){
        CampaignDto response = campaignService.addCampaign(campaignDto);
        if(response == null)
        {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CampaignDto> updateCampaign(@PathVariable Long id,@RequestBody CampaignDto campaignDto) {
        CampaignDto response = campaignService.updateCampaign(id,campaignDto);

        if(response == null)
        {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CampaignDto> deleteCampaign(@PathVariable Long id) {
        CampaignDto response = campaignService.deleteCampaign(id);

        if(response == null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/addProducts/{campaignId}")
    public ResponseEntity<CampaignDto> addProductsToCampaign(
            @PathVariable Long campaignId,
            @RequestBody List<Long> productIds) {
        CampaignDto response = campaignService.addProductsToCampaign(campaignId, productIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/removeProducts/{campaignId}")
    public ResponseEntity<CampaignDto> removeProductsFromCampaign(
            @PathVariable Long campaignId,
            @RequestBody List<Long> productIds) {
        CampaignDto response = campaignService.removeProductsFromCampaign(campaignId, productIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/removeAllProducts/{campaignId}")
    public ResponseEntity<CampaignDto> removeAllProductsFromCampaign(@PathVariable Long campaignId) {
        CampaignDto response = campaignService.removeAllProductsFromCampaign(campaignId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }





}
