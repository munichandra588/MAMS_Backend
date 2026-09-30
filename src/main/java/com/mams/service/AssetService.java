package com.mams.service;

import com.mams.entity.Asset;
import com.mams.entity.AssetCategory;
import com.mams.entity.Base;
import com.mams.exception.ResourceNotFoundException;
import com.mams.repository.AssetCategoryRepository;
import com.mams.repository.AssetRepository;
import com.mams.repository.BaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final BaseRepository baseRepository;
    private final AssetCategoryRepository categoryRepository;
    private final AuditLogService auditLogService;

    public AssetService(AssetRepository assetRepository, BaseRepository baseRepository,
                        AssetCategoryRepository categoryRepository, AuditLogService auditLogService) {
        this.assetRepository = assetRepository;
        this.baseRepository = baseRepository;
        this.categoryRepository = categoryRepository;
        this.auditLogService = auditLogService;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public List<Asset> getAssetsByBase(Long baseId) {
        return assetRepository.findByBaseId(baseId);
    }

    public List<Asset> getAssetsByCategory(Long categoryId) {
        return assetRepository.findByCategoryId(categoryId);
    }

    public List<Asset> getAssetsByBaseAndCategory(Long baseId, Long categoryId) {
        if (baseId != null && categoryId != null) {
            return assetRepository.findByBaseIdAndCategoryId(baseId, categoryId);
        } else if (baseId != null) {
            return assetRepository.findByBaseId(baseId);
        } else if (categoryId != null) {
            return assetRepository.findByCategoryId(categoryId);
        }
        return assetRepository.findAll();
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
    }

    public Asset createAsset(Asset asset, Long userId) {
        Base base = baseRepository.findById(asset.getBase().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Base not found"));
        AssetCategory category = categoryRepository.findById(asset.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        asset.setBase(base);
        asset.setCategory(category);
        Asset saved = assetRepository.save(asset);

        auditLogService.log(userId, "ADD_ASSET", "ASSET", saved.getId(),
                "Added asset: " + saved.getAssetName() + " (Qty: " + saved.getQuantity() + ")");

        return saved;
    }

    public Asset updateAsset(Long id, Asset updated, Long userId) {
        Asset existing = getAssetById(id);
        existing.setAssetCode(updated.getAssetCode());
        existing.setAssetName(updated.getAssetName());
        existing.setQuantity(updated.getQuantity());
        existing.setStatus(updated.getStatus());

        if (updated.getCategory() != null && updated.getCategory().getId() != null) {
            AssetCategory category = categoryRepository.findById(updated.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existing.setCategory(category);
        }
        if (updated.getBase() != null && updated.getBase().getId() != null) {
            Base base = baseRepository.findById(updated.getBase().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Base not found"));
            existing.setBase(base);
        }

        Asset saved = assetRepository.save(existing);

        auditLogService.log(userId, "UPDATE_ASSET", "ASSET", saved.getId(),
                "Updated asset: " + saved.getAssetName());

        return saved;
    }

    public List<AssetCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}
