package com.mams.controller;

import com.mams.entity.Asset;
import com.mams.entity.AssetCategory;
import com.mams.security.JwtUtil;
import com.mams.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssetController {

    private final AssetService assetService;
    private final JwtUtil jwtUtil;

    public AssetController(AssetService assetService, JwtUtil jwtUtil) {
        this.assetService = assetService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/assets")
    public ResponseEntity<List<Asset>> getAssets(
            @RequestParam(required = false) Long baseId,
            @RequestParam(required = false) Long categoryId) {
        List<Asset> assets = assetService.getAssetsByBaseAndCategory(baseId, categoryId);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/assets/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @PostMapping("/assets")
    @PreAuthorize("hasAnyRole('ADMIN', 'BASE_COMMANDER')")
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset,
                                             @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7));
        return ResponseEntity.ok(assetService.createAsset(asset, userId));
    }

    @PutMapping("/assets/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BASE_COMMANDER')")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset asset,
                                             @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7));
        return ResponseEntity.ok(assetService.updateAsset(id, asset, userId));
    }

    @GetMapping("/asset-categories")
    public ResponseEntity<List<AssetCategory>> getCategories() {
        return ResponseEntity.ok(assetService.getAllCategories());
    }
}
