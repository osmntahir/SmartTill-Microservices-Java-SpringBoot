package com.toyota.saleservice.resource;


import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.service.abstracts.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    @Autowired
    private final CampaignService campaignService;

    @GetMapping("/getAll")
    public PaginationResponse <CampaignDto> getAllCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") Double minDiscount,
            @RequestParam(required = false, defaultValue = 100 + "") Double maxDiscount,
            @RequestParam(defaultValue = "false") boolean deleted,
            @RequestParam(defaultValue = "") List<String> sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return campaignService.getCampaignsFiltered(page, size, name, minDiscount, maxDiscount, deleted, sortBy, sortDirection);
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




}
