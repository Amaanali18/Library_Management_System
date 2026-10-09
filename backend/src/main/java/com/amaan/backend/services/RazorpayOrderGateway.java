package com.amaan.backend.services;

import java.math.BigDecimal;

public interface RazorpayOrderGateway {

    String createOrder(
            BigDecimal amount,
            String currency,
            String receipt
    ) throws Exception;
}