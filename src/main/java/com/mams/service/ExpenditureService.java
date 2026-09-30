package com.mams.service;

import com.mams.entity.Asset;
import com.mams.entity.Base;
import com.mams.entity.Expenditure;
import com.mams.exception.ResourceNotFoundException;
import com.mams.repository.AssetRepository;
import com.mams.repository.BaseRepository;
import com.mams.repository.ExpenditureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;
    private final AssetRepository assetRepository;
    private final BaseRepository baseRepository;
    private final AuditLogService auditLogService;

    public ExpenditureService(ExpenditureRepository expenditureRepository, AssetRepository assetRepository,
                              BaseRepository baseRepository, AuditLogService auditLogService) {
        this.expenditureRepository = expenditureRepository;
        this.assetRepository = assetRepository;
        this.baseRepository = baseRepository;
        this.auditLogService = auditLogService;
    }

    public List<Expenditure> getAllExpenditures() {
        return expenditureRepository.findAll();
    }

    public List<Expenditure> getExpendituresByBase(Long baseId) {
        return expenditureRepository.findByBaseId(baseId);
    }

    @Transactional
    public Expenditure createExpenditure(Expenditure expenditure, Long userId) {
        if (expenditure.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Asset asset = assetRepository.findById(expenditure.getAsset().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        Base base = baseRepository.findById(expenditure.getBase().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Base not found"));

        if (asset.getQuantity() < expenditure.getQuantity()) {
            throw new RuntimeException("Insufficient asset quantity. Available: " + asset.getQuantity());
        }

        expenditure.setAsset(asset);
        expenditure.setBase(base);

        // Decrease inventory
        asset.setQuantity(asset.getQuantity() - expenditure.getQuantity());
        assetRepository.save(asset);

        Expenditure saved = expenditureRepository.save(expenditure);

        auditLogService.log(userId, "RECORD_EXPENDITURE", "EXPENDITURE", saved.getId(),
                "Expended " + asset.getAssetName() + " (Qty: " + expenditure.getQuantity() +
                ") at " + base.getBaseName() + " - Reason: " + expenditure.getReason());

        return saved;
    }
}
