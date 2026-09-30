package com.mams.controller;

import com.mams.entity.Purchase;
import com.mams.security.JwtUtil;
import com.mams.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@PreAuthorize("hasAnyRole('ADMIN', 'BASE_COMMANDER', 'LOGISTICS_OFFICER')")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final JwtUtil jwtUtil;

    public PurchaseController(PurchaseService purchaseService, JwtUtil jwtUtil) {
        this.purchaseService = purchaseService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getPurchases(@RequestParam(required = false) Long baseId) {
        if (baseId != null) {
            return ResponseEntity.ok(purchaseService.getPurchasesByBase(baseId));
        }
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(
            @RequestBody Purchase purchase,
            @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7));
        return ResponseEntity.ok(purchaseService.createPurchase(purchase, userId));
    }
}
