package com.amaan.backend.repository;

import com.amaan.backend.constants.BookStatus;
import com.amaan.backend.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {
    List<BookCopy> findByBookId(UUID bookId);
    Optional<BookCopy> findFirstByBookIdAndStatus(
            UUID bookId,
            BookStatus status
    );
    long countByBookId(UUID bookId);
    long countByBookIdAndStatus(UUID bookId, BookStatus status);
}
