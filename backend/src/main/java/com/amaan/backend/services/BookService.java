package com.amaan.backend.services;

import com.amaan.backend.dtos.request.BookRequest;
import com.amaan.backend.dtos.response.BookResponse;

import java.util.List;
import java.util.UUID;

public interface BookService {
    BookResponse createBook(BookRequest request);
    BookResponse getBook(UUID bookId);
    List<BookResponse> getAllBooks();
    List<BookResponse> searchBooks(String query);
    BookResponse updateBook(UUID bookId, BookRequest request);
    void deleteBook(UUID bookId);
}
