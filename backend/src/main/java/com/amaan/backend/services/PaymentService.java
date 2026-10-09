package com.amaan.backend.services;

import com.amaan.backend.dtos.request.PaymentRequest;
import com.amaan.backend.dtos.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createOrder(
            PaymentRequest request,
            String idempotencyKey
    );
}