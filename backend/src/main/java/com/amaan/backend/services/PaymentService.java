package com.amaan.backend.services;

import com.amaan.backend.dtos.request.PaymentChoiceRequest;
import com.amaan.backend.dtos.request.PaymentRequest;
import com.amaan.backend.dtos.response.PaymentResponse;
import com.amaan.backend.dtos.response.ReceiptResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse createOrder(
            PaymentRequest request,
            String idempotencyKey
    );

    PaymentResponse choosePaymentOption(
            UUID paymentId,
            PaymentChoiceRequest request
    );

    ReceiptResponse getReceipt(UUID paymentId);
}