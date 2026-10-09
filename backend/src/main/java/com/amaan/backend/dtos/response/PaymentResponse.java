package com.amaan.backend.dtos.response;

import com.amaan.backend.constants.PaymentProvider;
import com.amaan.backend.constants.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private UUID paymentId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private PaymentProvider provider;
}
