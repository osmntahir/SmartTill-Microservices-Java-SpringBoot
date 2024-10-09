package com.toyota.saleservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "campaign")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "discount_percentage")
    private Long discountPercentage;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "product_ids")
    private List<Long> productIds;

    private boolean deleted = false;

//    @PrePersist
//    public void prePersist() {
//        if (this.startDate == null) {
//            this.startDate = LocalDate.now();
//        }
//        if (this.endDate == null) {
//            this.endDate = this.startDate.plus(1, ChronoUnit.MONTHS);
//        }
//    }
}