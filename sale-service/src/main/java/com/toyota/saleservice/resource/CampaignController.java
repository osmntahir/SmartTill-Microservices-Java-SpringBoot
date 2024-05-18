package com.toyota.saleservice.resource;


import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.service.abstracts.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    @Autowired
    private final CampaignService campaignService;

    @GetMapping("/getAll")
    public PaginationResponse <CampaignDto> getAllCampaigns(
            @RequestParam(defaultValue = "") int page,
            @RequestParam(defaultValue = "") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") Double minDiscount,
            @RequestParam(required = false, defaultValue = 100 + "") Double maxDiscount,
            @RequestParam(defaultValue = "false") boolean deleted,
            @RequestParam(defaultValue = "") List<String> sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return campaignService.getCampaignsFiltered(page, size, name, minDiscount, maxDiscount, deleted, sortBy, sortDirection);
    }
    




}
