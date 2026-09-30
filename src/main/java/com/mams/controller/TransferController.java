package com.mams.controller;

import com.mams.entity.Transfer;
import com.mams.security.JwtUtil;
import com.mams.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@PreAuthorize("hasAnyRole('ADMIN', 'BASE_COMMANDER', 'LOGISTICS_OFFICER')")
public class TransferController {

    private final TransferService transferService;
    private final JwtUtil jwtUtil;

    public TransferController(TransferService transferService, JwtUtil jwtUtil) {
        this.transferService = transferService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Transfer>> getTransfers(@RequestParam(required = false) Long baseId) {
        if (baseId != null) {
            return ResponseEntity.ok(transferService.getTransfersByBase(baseId));
        }
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@RequestBody Transfer transfer,
                                                   @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7));
        return ResponseEntity.ok(transferService.createTransfer(transfer, userId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Transfer> completeTransfer(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7));
        return ResponseEntity.ok(transferService.completeTransfer(id, userId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Transfer> cancelTransfer(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7));
        return ResponseEntity.ok(transferService.cancelTransfer(id, userId));
    }
}
