package com.mams.service;

import com.mams.dto.DashboardSummary;
import com.mams.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final AssetRepository assetRepository;
    private final PurchaseRepository purchaseRepository;
    private final TransferRepository transferRepository;
    private final AssignmentRepository assignmentRepository;
    private final ExpenditureRepository expenditureRepository;

    public DashboardService(AssetRepository assetRepository, PurchaseRepository purchaseRepository,
                            TransferRepository transferRepository, AssignmentRepository assignmentRepository,
                            ExpenditureRepository expenditureRepository) {
        this.assetRepository = assetRepository;
        this.purchaseRepository = purchaseRepository;
        this.transferRepository = transferRepository;
        this.assignmentRepository = assignmentRepository;
        this.expenditureRepository = expenditureRepository;
    }

    public DashboardSummary getSummary(Long baseId) {
        long inStockAssets = assetRepository.getTotalQuantity(baseId);
        long purchases = purchaseRepository.getTotalPurchasedQuantity(baseId);
        long transferIn = baseId != null ? transferRepository.getTotalTransferIn(baseId) : transferRepository.getAllTransferIn(null);
        long transferOut = baseId != null ? transferRepository.getTotalTransferOut(baseId) : transferRepository.getAllTransferOut(null);
        long assigned = assignmentRepository.getTotalAssigned(baseId);
        long expended = expenditureRepository.getTotalExpended(baseId);

        // Section 24 Formula:
        // Net Movement = Purchases + Transfer In - Transfer Out
        long netMovement = purchases + transferIn - transferOut;

        // Closing Balance = Total assets currently under base custody (in-stock + assigned to personnel)
        long closingBalance = inStockAssets + assigned;

        // Opening Balance = Closing Balance - Net Movement + Expenditure
        // Guarantees: Opening Balance + Purchases + Transfer In - Transfer Out - Expenditure == Closing Balance
        long openingBalance = closingBalance - netMovement + expended;

        DashboardSummary summary = new DashboardSummary();
        summary.setOpeningBalance(openingBalance);
        summary.setClosingBalance(closingBalance);
        summary.setNetMovement(netMovement);
        summary.setPurchases(purchases);
        summary.setTransferIn(transferIn);
        summary.setTransferOut(transferOut);
        summary.setAssignedAssets(assigned);
        summary.setExpendedAssets(expended);

        return summary;
    }
}
