package com.amaan.backend.controller;

import com.amaan.backend.dtos.response.FineResponse;
import com.amaan.backend.services.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping("/calculate/{borrowRecordId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FineResponse> calculateFine(
            @PathVariable UUID borrowRecordId) {

        return ResponseEntity.ok(
                fineService.calculateFine(borrowRecordId)
        );
    }

    @GetMapping("/{fineId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FineResponse> getFine(
            @PathVariable UUID fineId) {

        return ResponseEntity.ok(
                fineService.getFine(fineId)
        );
    }

    @GetMapping("/borrow/{borrowRecordId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FineResponse> getFineByBorrowRecord(
            @PathVariable UUID borrowRecordId) {

        return ResponseEntity.ok(
                fineService.getFineByBorrowRecord(borrowRecordId)
        );
    }
}