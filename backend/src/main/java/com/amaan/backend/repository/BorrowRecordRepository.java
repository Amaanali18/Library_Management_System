package com.amaan.backend.repository;

import com.amaan.backend.constants.BorrowStatus;
import com.amaan.backend.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, UUID> {
    Optional<BorrowRecord> findByBookCopyIdAndBorrowStatus(
            UUID bookCopyId,
            BorrowStatus status
    );
    List<BorrowRecord> findByUserIdAndBorrowStatus(
            UUID userId,
            BorrowStatus status
    );
}
