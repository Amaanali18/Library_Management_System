package com.amaan.backend.services;

import com.amaan.backend.dtos.request.BorrowRequest;
import com.amaan.backend.dtos.response.BorrowResponse;

import java.util.UUID;

public interface BorrowService {
    BorrowResponse borrowBook(BorrowRequest request);
    BorrowResponse returnBook(UUID borrowRecordId);
}
