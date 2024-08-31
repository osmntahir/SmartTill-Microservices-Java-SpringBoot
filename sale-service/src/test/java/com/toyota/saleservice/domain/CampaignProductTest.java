package com.toyota.saleservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CampaignProductTest {

    @Test
    public void testNoArgsConstructor() {
        CampaignProduct campaignProduct = new CampaignProduct();
        assertThat(campaignProduct).isNotNull();
    }

    @Test
    public void testAllArgsConstructor() {
        Campaign campaign = new Campaign();
        Long productId = 1L;
        CampaignProduct campaignProduct = new CampaignProduct(1L, campaign, productId, false);

        assertThat(campaignProduct.getId()).isEqualTo(1L);
        assertThat(campaignProduct.getCampaign()).isEqualTo(campaign);
        assertThat(campaignProduct.getProductId()).isEqualTo(productId);
        assertThat(campaignProduct.isDeleted()).isFalse();
    }

    @Test
    public void testSettersAndGetters() {
        CampaignProduct campaignProduct = new CampaignProduct();
        Campaign campaign = new Campaign();
        Long productId = 1L;

        campaignProduct.setId(1L);
        campaignProduct.setCampaign(campaign);
        campaignProduct.setProductId(productId);
        campaignProduct.setDeleted(false);

        assertThat(campaignProduct.getId()).isEqualTo(1L);
        assertThat(campaignProduct.getCampaign()).isEqualTo(campaign);
        assertThat(campaignProduct.getProductId()).isEqualTo(productId);
        assertThat(campaignProduct.isDeleted()).isFalse();
    }
}
