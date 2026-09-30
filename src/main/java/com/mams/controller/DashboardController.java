package com.mams.controller;

import com.mams.dto.DashboardSummary;
import com.mams.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getSummary(@RequestParam(required = false) Long baseId) {
        return ResponseEntity.ok(dashboardService.getSummary(baseId));
    }
}
