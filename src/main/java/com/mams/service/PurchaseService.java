package com.mams.service;

import com.mams.entity.Asset;
import com.mams.entity.Base;
import com.mams.entity.Purchase;
import com.mams.exception.ResourceNotFoundException;
import com.mams.repository.AssetRepository;
import com.mams.repository.BaseRepository;
import com.mams.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final AssetRepository assetRepository;
    private final BaseRepository baseRepository;
    private final AuditLogService auditLogService;

    public PurchaseService(PurchaseRepository purchaseRepository, AssetRepository assetRepository,
                           BaseRepository baseRepository, AuditLogService auditLogService) {
        this.purchaseRepository = purchaseRepository;
        this.assetRepository = assetRepository;
        this.baseRepository = baseRepository;
        this.auditLogService = auditLogService;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public List<Purchase> getPurchasesByBase(Long baseId) {
        return purchaseRepository.findByBaseId(baseId);
    }

    @Transactional
    public Purchase createPurchase(Purchase purchase, Long userId) {
        if (purchase.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Asset asset = assetRepository.findById(purchase.getAsset().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        Base base = baseRepository.findById(purchase.getBase().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Base not found"));

        // Generate reference number
        purchase.setReferenceNumber("PUR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        purchase.setAsset(asset);
        purchase.setBase(base);

        // Save purchase
        Purchase saved = purchaseRepository.save(purchase);

        // Increase asset quantity
        asset.setQuantity(asset.getQuantity() + purchase.getQuantity());
        assetRepository.save(asset);

        // Audit log
        auditLogService.log(userId, "ADD_PURCHASE", "PURCHASE", saved.getId(),
                "Purchase: " + saved.getReferenceNumber() + " - " + asset.getAssetName() +
                " (Qty: " + purchase.getQuantity() + ") at " + base.getBaseName());

        return saved;
    }
}
