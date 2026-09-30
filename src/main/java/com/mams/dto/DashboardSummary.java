package com.mams.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DashboardSummary {
    private long openingBalance;
    private long closingBalance;
    private long netMovement;
    private long purchases;
    private long transferIn;
    private long transferOut;
    private long assignedAssets;
    private long expendedAssets;
}
