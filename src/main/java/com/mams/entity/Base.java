package com.mams.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_code", nullable = false, unique = true)
    private String baseCode;

    @Column(name = "base_name", nullable = false)
    private String baseName;

    private String location;

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
    private String status = "ACTIVE";
}
