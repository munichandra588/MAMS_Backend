package com.mams.controller;

import com.mams.entity.Expenditure;
import com.mams.security.JwtUtil;
import com.mams.service.ExpenditureService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenditures")
@PreAuthorize("hasAnyRole('ADMIN', 'BASE_COMMANDER')")
public class ExpenditureController {

    private final ExpenditureService expenditureService;
    private final JwtUtil jwtUtil;

    public ExpenditureController(ExpenditureService expenditureService, JwtUtil jwtUtil) {
        this.expenditureService = expenditureService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Expenditure>> getExpenditures(@RequestParam(required = false) Long baseId) {
        if (baseId != null) {
            return ResponseEntity.ok(expenditureService.getExpendituresByBase(baseId));
        }
        return ResponseEntity.ok(expenditureService.getAllExpenditures());
    }

    @PostMapping
    public ResponseEntity<Expenditure> createExpenditure(
            @RequestBody Expenditure expenditure,
            @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7));
        return ResponseEntity.ok(expenditureService.createExpenditure(expenditure, userId));
    }
}
