package com.mams.service;

import com.mams.entity.Asset;
import com.mams.entity.Assignment;
import com.mams.entity.Base;
import com.mams.exception.ResourceNotFoundException;
import com.mams.repository.AssetRepository;
import com.mams.repository.AssignmentRepository;
import com.mams.repository.BaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssetRepository assetRepository;
    private final BaseRepository baseRepository;
    private final AuditLogService auditLogService;

    public AssignmentService(AssignmentRepository assignmentRepository, AssetRepository assetRepository,
                             BaseRepository baseRepository, AuditLogService auditLogService) {
        this.assignmentRepository = assignmentRepository;
        this.assetRepository = assetRepository;
        this.baseRepository = baseRepository;
        this.auditLogService = auditLogService;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> getAssignmentsByBase(Long baseId) {
        return assignmentRepository.findByBaseId(baseId);
    }

    @Transactional
    public Assignment createAssignment(Assignment assignment, Long userId) {
        if (assignment.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Asset asset = assetRepository.findById(assignment.getAsset().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        Base base = baseRepository.findById(assignment.getBase().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Base not found"));

        if (asset.getQuantity() < assignment.getQuantity()) {
            throw new RuntimeException("Insufficient asset quantity. Available: " + asset.getQuantity());
        }

        assignment.setAsset(asset);
        assignment.setBase(base);

        // Decrease available quantity
        asset.setQuantity(asset.getQuantity() - assignment.getQuantity());
        assetRepository.save(asset);

        Assignment saved = assignmentRepository.save(assignment);

        auditLogService.log(userId, "ASSIGN_ASSET", "ASSIGNMENT", saved.getId(),
                "Assigned " + asset.getAssetName() + " (Qty: " + assignment.getQuantity() +
                ") to " + assignment.getPersonnelName() + " at " + base.getBaseName());

        return saved;
    }
}
