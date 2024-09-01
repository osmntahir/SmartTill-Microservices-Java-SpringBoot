package com.toyota.saleservice.domain;



import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Campaign campaign;

    @Column(name = "product_id")
    private Long productId;

    boolean deleted = Boolean.FALSE;

}
