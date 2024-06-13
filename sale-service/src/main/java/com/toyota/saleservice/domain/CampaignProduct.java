package com.toyota.saleservice.domain;

import com.toyota.productservice.domain.Product;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "campaign_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Where(clause = "deleted = false")
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

    boolean deleted = Boolean.FALSE;
    // Getter ve setter metotları
}
