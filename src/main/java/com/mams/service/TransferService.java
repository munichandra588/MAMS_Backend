package com.mams.service;

import com.mams.entity.Asset;
import com.mams.entity.Base;
import com.mams.entity.Transfer;
import com.mams.exception.ResourceNotFoundException;
import com.mams.repository.AssetRepository;
import com.mams.repository.BaseRepository;
import com.mams.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AssetRepository assetRepository;
    private final BaseRepository baseRepository;
    private final AuditLogService auditLogService;

    public TransferService(TransferRepository transferRepository, AssetRepository assetRepository,
                           BaseRepository baseRepository, AuditLogService auditLogService) {
        this.transferRepository = transferRepository;
        this.assetRepository = assetRepository;
        this.baseRepository = baseRepository;
        this.auditLogService = auditLogService;
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAllByOrderByIdDesc();
    }

    public List<Transfer> getTransfersByBase(Long baseId) {
        return transferRepository.findByFromBaseIdOrToBaseIdOrderByIdDesc(baseId, baseId);
    }

    @Transactional
    public Transfer createTransfer(Transfer transfer, Long userId) {
        // Validations
        if (transfer.getQuantity() == null || transfer.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Asset sourceAsset = assetRepository.findById(transfer.getAsset().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        // If fromBase is not provided, use asset's base
        Base fromBase = transfer.getFromBase() != null && transfer.getFromBase().getId() != null
                ? baseRepository.findById(transfer.getFromBase().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Source base not found"))
                : sourceAsset.getBase();

        Base toBase = baseRepository.findById(transfer.getToBase().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination base not found"));

        if (fromBase.getId().equals(toBase.getId())) {
            throw new RuntimeException("Source and destination base cannot be the same");
        }

        // Verify asset actually belongs to source base
        if (!sourceAsset.getBase().getId().equals(fromBase.getId())) {
            throw new RuntimeException("Asset '" + sourceAsset.getAssetName() + "' is not stationed at " + fromBase.getBaseName());
        }

        // Check if source has enough quantity
        if (sourceAsset.getQuantity() < transfer.getQuantity()) {
            throw new RuntimeException("Insufficient asset quantity at " + fromBase.getBaseName() +
                    ". Available: " + sourceAsset.getQuantity() + ", requested: " + transfer.getQuantity());
        }

        transfer.setReferenceNumber("TRF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        transfer.setAsset(sourceAsset);
        transfer.setFromBase(fromBase);
        transfer.setToBase(toBase);

        boolean isImmediate = "COMPLETED".equalsIgnoreCase(transfer.getStatus());

        if (isImmediate) {
            // Immediate inventory movement
            sourceAsset.setQuantity(sourceAsset.getQuantity() - transfer.getQuantity());
            assetRepository.save(sourceAsset);

            Asset destAsset = assetRepository
                    .findByAssetCodeAndBaseId(sourceAsset.getAssetCode(), toBase.getId())
                    .orElseGet(() -> {
                        Asset newAsset = new Asset();
                        newAsset.setAssetCode(sourceAsset.getAssetCode());
                        newAsset.setAssetName(sourceAsset.getAssetName());
                        newAsset.setCategory(sourceAsset.getCategory());
                        newAsset.setBase(toBase);
                        newAsset.setQuantity(0);
                        newAsset.setStatus("AVAILABLE");
                        return newAsset;
                    });

            destAsset.setQuantity(destAsset.getQuantity() + transfer.getQuantity());
            assetRepository.save(destAsset);

            transfer.setStatus("COMPLETED");
        } else {
            transfer.setStatus("PENDING");
        }

        Transfer saved = transferRepository.save(transfer);

        String actionName = isImmediate ? "COMPLETE_TRANSFER" : "CREATE_TRANSFER";
        String desc = (isImmediate ? "Transferred: " : "Transfer requested: ") + saved.getReferenceNumber() +
                " - " + sourceAsset.getAssetName() + " (Qty: " + transfer.getQuantity() +
                ") from " + fromBase.getBaseName() + " to " + toBase.getBaseName();
        auditLogService.log(userId, actionName, "TRANSFER", saved.getId(), desc);

        return saved;
    }

    @Transactional
    public Transfer completeTransfer(Long id, Long userId) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));

        if (!"PENDING".equals(transfer.getStatus())) {
            throw new RuntimeException("Transfer is not in PENDING status (current: " + transfer.getStatus() + ")");
        }

        Asset sourceAsset = transfer.getAsset();

        // Verify quantity still available
        if (sourceAsset.getQuantity() < transfer.getQuantity()) {
            throw new RuntimeException("Insufficient asset quantity at source base. Available: " +
                    sourceAsset.getQuantity() + ", Required: " + transfer.getQuantity());
        }

        // Decrease source asset quantity
        sourceAsset.setQuantity(sourceAsset.getQuantity() - transfer.getQuantity());
        assetRepository.save(sourceAsset);

        // Find or create asset at destination base
        Asset destAsset = assetRepository
                .findByAssetCodeAndBaseId(sourceAsset.getAssetCode(), transfer.getToBase().getId())
                .orElseGet(() -> {
                    Asset newAsset = new Asset();
                    newAsset.setAssetCode(sourceAsset.getAssetCode());
                    newAsset.setAssetName(sourceAsset.getAssetName());
                    newAsset.setCategory(sourceAsset.getCategory());
                    newAsset.setBase(transfer.getToBase());
                    newAsset.setQuantity(0);
                    newAsset.setStatus("AVAILABLE");
                    return newAsset;
                });

        destAsset.setQuantity(destAsset.getQuantity() + transfer.getQuantity());
        assetRepository.save(destAsset);

        transfer.setStatus("COMPLETED");
        Transfer saved = transferRepository.save(transfer);

        auditLogService.log(userId, "COMPLETE_TRANSFER", "TRANSFER", saved.getId(),
                "Transfer completed: " + saved.getReferenceNumber() + " - " + sourceAsset.getAssetName() +
                " (Qty: " + transfer.getQuantity() + ") now at " + transfer.getToBase().getBaseName());

        return saved;
    }

    @Transactional
    public Transfer cancelTransfer(Long id, Long userId) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));

        if (!"PENDING".equals(transfer.getStatus())) {
            throw new RuntimeException("Only PENDING transfers can be cancelled");
        }

        transfer.setStatus("CANCELLED");
        Transfer saved = transferRepository.save(transfer);

        auditLogService.log(userId, "CANCEL_TRANSFER", "TRANSFER", saved.getId(),
                "Transfer cancelled: " + saved.getReferenceNumber());

        return saved;
    }
}
