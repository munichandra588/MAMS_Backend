package com.mams.controller;

import com.mams.entity.Assignment;
import com.mams.security.JwtUtil;
import com.mams.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@PreAuthorize("hasAnyRole('ADMIN', 'BASE_COMMANDER')")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final JwtUtil jwtUtil;

    public AssignmentController(AssignmentService assignmentService, JwtUtil jwtUtil) {
        this.assignmentService = assignmentService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Assignment>> getAssignments(@RequestParam(required = false) Long baseId) {
        if (baseId != null) {
            return ResponseEntity.ok(assignmentService.getAssignmentsByBase(baseId));
        }
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @RequestBody Assignment assignment,
            @RequestHeader("Authorization") String auth) {
        Long userId = jwtUtil.getUserIdFromToken(auth.substring(7));
        return ResponseEntity.ok(assignmentService.createAssignment(assignment, userId));
    }
}
