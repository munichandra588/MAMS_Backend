package com.mams.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenditures")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Expenditure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_id", nullable = false)
    private Base base;

    @Column(nullable = false)
    private Integer quantity;

    private String reason;

    @Column(name = "expenditure_date", nullable = false)
    private LocalDate expenditureDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
