package com.toyota.saleservice.domain;


import com.toyota.productservice.domain.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;


@Entity
@Table(name = "sold_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Where(clause = "deleted = false")
public class SoldProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    private boolean deleted = Boolean.FALSE;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private String name;
    private double price;
    private int quantity;
    private double total;
    private long discount;

}
