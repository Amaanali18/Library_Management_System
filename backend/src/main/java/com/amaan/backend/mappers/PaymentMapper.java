package com.amaan.backend.mappers;

import com.amaan.backend.dtos.response.PaymentResponse;
import com.amaan.backend.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getProviderOrderId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .provider(payment.getProvider())
                .build();
    }
}