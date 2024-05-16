package com.toyota.saleservice.service.impl;

import com.toyota.saleservice.dao.CampaignRepository;
import com.toyota.saleservice.service.abstracts.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    @Autowired
    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

}
