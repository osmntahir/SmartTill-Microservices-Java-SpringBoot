package com.toyota.saleservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

import java.util.List;

@Entity
@AllArgsConstructor
@Table(name = "sale")
@NoArgsConstructor
@Data

@Where(clause = "deleted = false") // Hibernate specific annotation
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double totalPrice;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;
    private boolean deleted = Boolean.FALSE;

    @Column(name = "cashier_name")
    private String cashierName;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SoldProduct> soldProducts;


    @PrePersist
    protected void onCreate() {
        date = LocalDateTime.now(); // Set the date of creation
    }



}
