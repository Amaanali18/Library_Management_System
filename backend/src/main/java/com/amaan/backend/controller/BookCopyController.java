package com.amaan.backend.controller;

import com.amaan.backend.dtos.response.BookCopyResponse;
import com.amaan.backend.services.BookCopyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookCopyController {

    private final BookCopyService bookCopyService;
    public BookCopyController(BookCopyService bookCopyService){
        this.bookCopyService = bookCopyService;
    }

    @GetMapping("/{bookId}/copies")
    @PreAuthorize("hasAnyRole('USER','LIBRARIAN','ADMIN')")
    public ResponseEntity<List<BookCopyResponse>> getCopies(
            @PathVariable UUID bookId) {

        return ResponseEntity.ok(
                bookCopyService.getCopies(bookId)
        );
    }

    @PostMapping("/{bookId}/copies")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<BookCopyResponse> addCopy(
            @PathVariable UUID bookId) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookCopyService.addCopy(bookId));
    }

    @DeleteMapping("/copies/{copyId}")
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<Void> deleteCopy(
            @PathVariable UUID copyId) {

        bookCopyService.deleteCopy(copyId);
        return ResponseEntity.noContent().build();
    }
}
