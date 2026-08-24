package com.amaan.backend.dtos.response;

import com.amaan.backend.constants.BorrowStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowResponse {
    private UUID bookCopyId;
    private BorrowStatus borrowStatus;
    private Instant dueDate;
}
