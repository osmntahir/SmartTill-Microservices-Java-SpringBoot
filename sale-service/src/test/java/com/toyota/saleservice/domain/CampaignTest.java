//package com.toyota.saleservice.domain;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CampaignTest {
//
//    @Test
//    public void testNoArgsConstructor() {
//        Campaign campaign = new Campaign();
//        assertThat(campaign).isNotNull();
//    }
//
//    @Test
//    public void testAllArgsConstructor() {
//        List<CampaignProduct> campaignProducts = new ArrayList<>();
//        Campaign campaign = new Campaign(1L, "Summer Sale", 20L, "Discount on summer products", false, campaignProducts);
//
//        assertThat(campaign.getId()).isEqualTo(1L);
//        assertThat(campaign.getName()).isEqualTo("Summer Sale");
//        assertThat(campaign.getDiscount()).isEqualTo(20L);
//        assertThat(campaign.getDescription()).isEqualTo("Discount on summer products");
//        assertThat(campaign.isDeleted()).isFalse();
//        assertThat(campaign.getCampaignProducts()).isEqualTo(campaignProducts);
//    }
//
//    @Test
//    public void testSettersAndGetters() {
//        Campaign campaign = new Campaign();
//        List<CampaignProduct> campaignProducts = new ArrayList<>();
//
//        campaign.setId(1L);
//        campaign.setName("Summer Sale");
//        campaign.setDiscount(20L);
//        campaign.setDescription("Discount on summer products");
//        campaign.setDeleted(false);
//        campaign.setCampaignProducts(campaignProducts);
//
//        assertThat(campaign.getId()).isEqualTo(1L);
//        assertThat(campaign.getName()).isEqualTo("Summer Sale");
//        assertThat(campaign.getDiscount()).isEqualTo(20L);
//        assertThat(campaign.getDescription()).isEqualTo("Discount on summer products");
//        assertThat(campaign.isDeleted()).isFalse();
//        assertThat(campaign.getCampaignProducts()).isEqualTo(campaignProducts);
//    }
//}
