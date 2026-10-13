package com.amaan.backend.controller;

import com.amaan.backend.dtos.request.PaymentChoiceRequest;
import com.amaan.backend.dtos.request.PaymentRequest;
import com.amaan.backend.dtos.response.PaymentResponse;
import com.amaan.backend.dtos.response.ReceiptResponse;
import com.amaan.backend.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> createOrder(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.createOrder(
                        request,
                        idempotencyKey
                ));
    }


    @PostMapping("/{paymentId}/choice")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> choosePaymentOption(
            @PathVariable UUID paymentId,
            @Valid @RequestBody PaymentChoiceRequest request
    ) {
        return ResponseEntity.ok(
                paymentService.choosePaymentOption(paymentId, request)
        );
    }


    @GetMapping("/{paymentId}/receipt")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReceiptResponse> getReceipt(
            @PathVariable UUID paymentId
    ) {
        return ResponseEntity.ok(
                paymentService.getReceipt(paymentId)
        );
    }


}
