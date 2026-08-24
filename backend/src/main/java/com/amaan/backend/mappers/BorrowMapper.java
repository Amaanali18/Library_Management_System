package com.amaan.backend.mappers;

import com.amaan.backend.dtos.response.BorrowResponse;
import com.amaan.backend.entity.BorrowRecord;
import org.springframework.stereotype.Component;

@Component
public class BorrowMapper {
    public BorrowResponse toResponse(BorrowRecord record) {
        return BorrowResponse.builder()
                .bookCopyId(record.getBookCopy().getId())
                .borrowStatus(record.getBorrowStatus())
                .dueDate(record.getDueDate())
                .build();
    }
}
