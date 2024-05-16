package com.toyota.saleservice.domain;

import com.toyota.productservice.domain.Product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "campaign_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CampaignProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Getter ve setter metotları
}
