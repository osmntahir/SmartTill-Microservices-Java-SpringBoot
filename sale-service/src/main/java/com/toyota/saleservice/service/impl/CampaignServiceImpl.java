package com.toyota.saleservice.service.impl;

import com.toyota.saleservice.dao.CampaignRepository;
import com.toyota.saleservice.domain.Campaign;
import com.toyota.saleservice.dto.CampaignDto;
import com.toyota.saleservice.dto.PaginationResponse;
import com.toyota.saleservice.exception.CampaignAlreadyExistsException;
import com.toyota.saleservice.service.abstracts.CampaignService;
import com.toyota.saleservice.service.common.MapUtil;
import com.toyota.saleservice.service.common.SortUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final Logger logger = LogManager.getLogger(CampaignService.class);
    private final MapUtil mapUtil;

    @Override
    public PaginationResponse<CampaignDto> getCampaignsFiltered(int page, int size, String name, Double minDiscount, Double maxDiscount, boolean deleted, List<String> sortBy, String sortOrder) {
        logger.info("Getting campaigns with filters");
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtil.createSortOrder(sortBy, sortOrder)));
        Page<Campaign> pageResponse=campaignRepository.getCampaignsFiltered(name, minDiscount,
                maxDiscount, deleted, pageable);
        logger.debug("Retrieved {} campaigns.",pageResponse.getContent().size());
        List<CampaignDto>campaignDtos=pageResponse.stream().map(
                mapUtil::convertCampaignToCampaignDto).collect(Collectors.toList());
        logger.info("Retrieved and converted {} campaigns to dto.",campaignDtos.size());

        return new PaginationResponse<>(campaignDtos,pageResponse);



    }

    @Override
    public CampaignDto addCampaign(CampaignDto campaignDto) {
        logger.info("Adding campaign with name: {}",campaignDto.getName());
        if(campaignRepository.existsByName(campaignDto.getName())){
            logger.warn("Vehicle create failed due to VIN conflict: Vehicle with this VIN already exists!" +
                    " VIN: {}",campaignDto.getName());
            throw new CampaignAlreadyExistsException("Campaign with this name already exists! " + campaignDto.getName());
        }
        Campaign campaign = mapUtil.convertCampaignDtoToCampaign(campaignDto);
        Campaign saved = campaignRepository.save(campaign);
        logger.info("Campaign with name {} added successfully.",campaignDto.getName());

        return mapUtil.convertCampaignToCampaignDto(saved);
    }
}