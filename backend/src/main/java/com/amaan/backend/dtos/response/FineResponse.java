package com.amaan.backend.dtos.response;

import com.amaan.backend.constants.FineStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineResponse {

    private UUID id;
    private UUID borrowRecordId;
    private BigDecimal amount;
    private long daysOverdue;
    private FineStatus status;
    private Instant createdAt;
    private Instant paidAt;
}