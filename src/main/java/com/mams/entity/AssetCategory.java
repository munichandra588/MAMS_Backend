package com.mams.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asset_categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AssetCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
