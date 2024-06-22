package com.toyota.saleservice.domain;

import com.toyota.productservice.domain.Product;
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
        Product product = new Product();
        CampaignProduct campaignProduct = new CampaignProduct(1L, campaign, product, false);

        assertThat(campaignProduct.getId()).isEqualTo(1L);
        assertThat(campaignProduct.getCampaign()).isEqualTo(campaign);
        assertThat(campaignProduct.getProduct()).isEqualTo(product);
        assertThat(campaignProduct.isDeleted()).isFalse();
    }

    @Test
    public void testSettersAndGetters() {
        CampaignProduct campaignProduct = new CampaignProduct();
        Campaign campaign = new Campaign();
        Product product = new Product();

        campaignProduct.setId(1L);
        campaignProduct.setCampaign(campaign);
        campaignProduct.setProduct(product);
        campaignProduct.setDeleted(false);

        assertThat(campaignProduct.getId()).isEqualTo(1L);
        assertThat(campaignProduct.getCampaign()).isEqualTo(campaign);
        assertThat(campaignProduct.getProduct()).isEqualTo(product);
        assertThat(campaignProduct.isDeleted()).isFalse();
    }
}
