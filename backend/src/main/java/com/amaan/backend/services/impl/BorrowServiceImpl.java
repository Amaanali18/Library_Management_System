package com.amaan.backend.services.impl;

import com.amaan.backend.constants.BookStatus;
import com.amaan.backend.constants.BorrowStatus;
import com.amaan.backend.dtos.request.BorrowRequest;
import com.amaan.backend.dtos.response.BorrowResponse;
import com.amaan.backend.entity.Book;
import com.amaan.backend.entity.BookCopy;
import com.amaan.backend.entity.BorrowRecord;
import com.amaan.backend.entity.User;
import com.amaan.backend.mappers.BorrowMapper;
import com.amaan.backend.repository.BookCopyRepository;
import com.amaan.backend.repository.BookRepository;
import com.amaan.backend.repository.BorrowRecordRepository;
import com.amaan.backend.security.userdetails.CustomUserDetails;
import com.amaan.backend.services.BorrowService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final BorrowMapper borrowMapper;
    public BorrowServiceImpl(
            BorrowRecordRepository borrowRecordRepository,
            BookRepository bookRepository,
            BookCopyRepository bookCopyRepository,
            BorrowMapper borrowMapper) {

        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.borrowMapper = borrowMapper;
    }

    @Override
    @Transactional
    public BorrowResponse borrowBook(BorrowRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));
        BookCopy copy = bookCopyRepository.findFirstByBookIdAndStatus(
                        book.getId(),
                        BookStatus.AVAILABLE
                ).orElseThrow(() -> new RuntimeException("No available copies"));
        Instant borrowDate = Instant.now();
        Instant dueDate = borrowDate.plus(14, ChronoUnit.DAYS);
        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBookCopy(copy);
        record.setBorrowDate(borrowDate);
        record.setDueDate(dueDate);
        record.setBorrowStatus(BorrowStatus.BORROWED);
        copy.setStatus(BookStatus.BORROWED);
        borrowRecordRepository.save(record);
        bookCopyRepository.save(copy);
        return borrowMapper.toResponse(record);
    }

    @Override
    @Transactional
    public BorrowResponse returnBook(UUID borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId).orElseThrow(() -> new RuntimeException("Borrow record not found"));
        if (record.getBorrowStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Book already returned");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        if (!record.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot return this book");
        }
        record.setReturnDate(Instant.now());
        record.setBorrowStatus(BorrowStatus.RETURNED);
        BookCopy copy = record.getBookCopy();
        copy.setStatus(BookStatus.AVAILABLE);
        borrowRecordRepository.save(record);
        bookCopyRepository.save(copy);
        return borrowMapper.toResponse(record);
    }
}