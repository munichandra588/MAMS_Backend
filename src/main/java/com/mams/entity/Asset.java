package com.mams.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets", uniqueConstraints = {
    @UniqueConstraint(name = "UK_asset_code_base", columnNames = {"asset_code", "base_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_code", nullable = false)
    private String assetCode;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private AssetCategory category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_id", nullable = false)
    private Base base;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'AVAILABLE'")
    private String status = "AVAILABLE";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
