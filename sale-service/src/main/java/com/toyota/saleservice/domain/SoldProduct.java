package com.toyota.saleservice.domain;




import com.toyota.productservice.domain.Product;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "sold_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoldProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    /**
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
     */

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;


}
