package com.amaan.backend.services;

import com.amaan.backend.dtos.response.FineResponse;

import java.util.UUID;

public interface FineService {

    FineResponse calculateFine(UUID borrowRecordId);

    FineResponse getFine(UUID fineId);

    FineResponse getFineByBorrowRecord(UUID borrowRecordId);
}