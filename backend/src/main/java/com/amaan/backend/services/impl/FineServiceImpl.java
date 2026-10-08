package com.amaan.backend.services.impl;

import com.amaan.backend.constants.BorrowStatus;
import com.amaan.backend.constants.FineStatus;
import com.amaan.backend.dtos.response.FineResponse;
import com.amaan.backend.entity.BorrowRecord;
import com.amaan.backend.entity.Fine;
import com.amaan.backend.entity.User;
import com.amaan.backend.mappers.FineMapper;
import com.amaan.backend.repository.BorrowRecordRepository;
import com.amaan.backend.repository.FineRepository;
import com.amaan.backend.security.userdetails.CustomUserDetails;
import com.amaan.backend.services.FineService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final FineMapper fineMapper;

    @Value("${app.fine.daily-rate}")
    private BigDecimal dailyFineRate;

    public FineServiceImpl(
            FineRepository fineRepository,
            BorrowRecordRepository borrowRecordRepository,
            FineMapper fineMapper) {

        this.fineRepository = fineRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.fineMapper = fineMapper;
    }

    @Override
    @Transactional
    public FineResponse calculateFine(UUID borrowRecordId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        BorrowRecord record = borrowRecordRepository
                .findById(borrowRecordId)
                .orElseThrow(() ->
                        new RuntimeException("Borrow record not found"));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot access this borrowing record"
            );
        }

        Instant endDate = record.getReturnDate() != null
                ? record.getReturnDate()
                : Instant.now();

        if (!endDate.isAfter(record.getDueDate())) {
            throw new RuntimeException("Book is not overdue");
        }

        long daysOverdue = ChronoUnit.DAYS.between(
                record.getDueDate(),
                endDate
        );

        BigDecimal amount = dailyFineRate
                .multiply(BigDecimal.valueOf(daysOverdue))
                .setScale(2, RoundingMode.HALF_UP);

        Fine fine = fineRepository
                .findByBorrowRecordId(borrowRecordId)
                .orElse(null);

        if (fine == null) {

            fine = Fine.builder()
                    .borrowRecord(record)
                    .amount(amount)
                    .daysOverdue(daysOverdue)
                    .status(FineStatus.UNPAID)
                    .build();

        } else if (fine.getStatus() != FineStatus.PAID) {

            fine.setAmount(amount);
            fine.setDaysOverdue(daysOverdue);
        }

        fineRepository.save(fine);

        return fineMapper.toResponse(fine);
    }

    @Override
    public FineResponse getFine(UUID fineId) {

        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() ->
                        new RuntimeException("Fine not found"));

        return fineMapper.toResponse(fine);
    }

    @Override
    public FineResponse getFineByBorrowRecord(UUID borrowRecordId) {

        Fine fine = fineRepository
                .findByBorrowRecordId(borrowRecordId)
                .orElseThrow(() ->
                        new RuntimeException("Fine not found"));

        return fineMapper.toResponse(fine);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void processOverdueFines() {

        Instant now = Instant.now();

        List<BorrowRecord> records =
                borrowRecordRepository.findByBorrowStatus(
                        BorrowStatus.BORROWED
                );

        for (BorrowRecord record : records) {

            if (record.getDueDate().isBefore(now)) {

                long daysOverdue = ChronoUnit.DAYS.between(
                        record.getDueDate(),
                        now
                );

                BigDecimal amount = dailyFineRate
                        .multiply(BigDecimal.valueOf(daysOverdue))
                        .setScale(2, RoundingMode.HALF_UP);

                Fine fine = fineRepository
                        .findByBorrowRecordId(record.getId())
                        .orElse(null);

                if (fine == null) {

                    fine = Fine.builder()
                            .borrowRecord(record)
                            .amount(amount)
                            .daysOverdue(daysOverdue)
                            .status(FineStatus.UNPAID)
                            .build();

                } else if (fine.getStatus() != FineStatus.PAID) {

                    fine.setAmount(amount);
                    fine.setDaysOverdue(daysOverdue);
                }

                fineRepository.save(fine);
            }
        }
    }
}