package com.amaan.backend.controller;

import com.amaan.backend.dtos.request.BookRequest;
import com.amaan.backend.dtos.response.BookResponse;
import com.amaan.backend.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public ResponseEntity<BookResponse> addBook(@RequestBody @Valid BookRequest bookRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookRequest));
    }

    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN', 'ADMIN')")
    @GetMapping()
    public ResponseEntity<List<BookResponse>> getBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN', 'ADMIN')")
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> getBook(@PathVariable UUID bookId) {
        return ResponseEntity.ok(bookService.getBook(bookId));
    }

    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN', 'ADMIN')")
    @GetMapping("/queryBook")
    public ResponseEntity<List<BookResponse>> searchBook(@RequestParam("query") String query) {
        return ResponseEntity.ok(bookService.searchBooks(query));
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID bookId , @RequestBody @Valid BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.updateBook(bookId, bookRequest));
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
