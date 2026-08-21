package com.amaan.backend.services.impl;

import com.amaan.backend.dtos.request.BookRequest;
import com.amaan.backend.dtos.response.BookResponse;
import com.amaan.backend.entity.Book;
import com.amaan.backend.mappers.BookMapper;
import com.amaan.backend.repository.BookRepository;
import com.amaan.backend.services.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookResponse createBook(BookRequest request) {
        Book book = bookMapper.toEntity(request);
        bookRepository.save(book);
        return bookMapper.toResponse(book);
    }

    @Override
    public BookResponse getBook(UUID bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        return bookMapper.toResponse(book);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toResponse).toList();
    }

    @Override
    public List<BookResponse> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query,query)
                .stream().map(bookMapper::toResponse).toList();
    }

    @Override
    public BookResponse updateBook(UUID bookId, BookRequest request) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setContent(request.getContent());
        book.setDescription(request.getDescription());
        book.setPublishDate(request.getPublishDate());
        book.setPages(request.getPages());
        book.setPreviewContent(request.getPreviewContent());
        bookRepository.save(book);
        return bookMapper.toResponse(book);
    }

    @Override
    public void deleteBook(UUID bookId) {
        // till v2 gets built
        bookRepository.deleteById(bookId);
    }
}
