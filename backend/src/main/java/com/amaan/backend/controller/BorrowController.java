package com.amaan.backend.controller;

import com.amaan.backend.dtos.request.BorrowRequest;
import com.amaan.backend.dtos.response.BorrowResponse;
import com.amaan.backend.services.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BorrowResponse> borrowBook(
            @Valid @RequestBody BorrowRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(borrowService.borrowBook(request));
    }

    @PostMapping("/{borrowRecordId}/return")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BorrowResponse> returnBook(
            @PathVariable UUID borrowRecordId) {
        return ResponseEntity.ok(
                borrowService.returnBook(borrowRecordId)
        );
    }
}
