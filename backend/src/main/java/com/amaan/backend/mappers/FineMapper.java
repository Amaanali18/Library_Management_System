package com.amaan.backend.mappers;

import com.amaan.backend.dtos.response.FineResponse;
import com.amaan.backend.entity.Fine;
import org.springframework.stereotype.Component;

@Component
public class FineMapper {

    public FineResponse toResponse(Fine fine) {

        return FineResponse.builder()
                .id(fine.getId())
                .borrowRecordId(fine.getBorrowRecord().getId())
                .amount(fine.getAmount())
                .daysOverdue(fine.getDaysOverdue())
                .status(fine.getStatus())
                .createdAt(fine.getCreatedAt())
                .paidAt(fine.getPaidAt())
                .build();
    }
}