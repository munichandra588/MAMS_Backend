package com.mams.controller;

import com.mams.entity.Base;
import com.mams.service.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bases")
public class BaseController {

    private final BaseService baseService;

    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public ResponseEntity<List<Base>> getAllBases() {
        return ResponseEntity.ok(baseService.getAllBases());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Base> createBase(@RequestBody Base base) {
        return ResponseEntity.ok(baseService.createBase(base));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Base> updateBase(@PathVariable Long id, @RequestBody Base base) {
        return ResponseEntity.ok(baseService.updateBase(id, base));
    }
}
