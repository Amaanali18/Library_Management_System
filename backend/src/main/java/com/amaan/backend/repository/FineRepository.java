package com.amaan.backend.repository;

import com.amaan.backend.constants.BorrowStatus;
import com.amaan.backend.entity.BorrowRecord;
import com.amaan.backend.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FineRepository extends JpaRepository<Fine, UUID> {

    Optional<Fine> findByBorrowRecordId(UUID borrowRecordId);
    List<BorrowRecord> findByBorrowStatus(BorrowStatus status);
}