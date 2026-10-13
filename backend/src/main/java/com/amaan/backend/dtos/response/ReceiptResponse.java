
package com.amaan.backend.dtos.response;

import com.amaan.backend.constants.FineStatus;
import com.amaan.backend.constants.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReceiptResponse(
        String receiptNumber,
        UUID paymentId,
        UUID fineId,

        // Borrower
        UUID borrowerId,

        // Book details
        UUID bookId,
        UUID bookCopyId,
        String bookTitle,
        String author,
        String isbn,

        // Fine and payment details
        BigDecimal fineAmount,
        String currency,
        long daysOverdue,
        FineStatus fineStatus,
        PaymentStatus paymentStatus,

        // Dates
        Instant borrowDate,
        Instant dueDate,
        Instant returnDate,
        Instant receiptGeneratedAt,
        Instant paidAt
) {}
