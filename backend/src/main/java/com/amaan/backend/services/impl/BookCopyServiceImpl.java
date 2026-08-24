package com.amaan.backend.services.impl;

import com.amaan.backend.constants.BookStatus;
import com.amaan.backend.dtos.response.BookCopyResponse;
import com.amaan.backend.entity.Book;
import com.amaan.backend.entity.BookCopy;
import com.amaan.backend.mappers.BookCopyMapper;
import com.amaan.backend.repository.BookCopyRepository;
import com.amaan.backend.repository.BookRepository;
import com.amaan.backend.services.BookCopyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;
    private final BookCopyMapper bookCopyMapper;
    public BookCopyServiceImpl(
            BookCopyRepository bookCopyRepository,
            BookRepository bookRepository,
            BookCopyMapper bookCopyMapper) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
        this.bookCopyMapper = bookCopyMapper;
    }

    @Override
    public BookCopyResponse addCopy(UUID bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        BookCopy copy = new BookCopy();
        copy.setBook(book);
        copy.setStatus(BookStatus.AVAILABLE);
        bookCopyRepository.save(copy);
        return bookCopyMapper.toResponse(copy);
    }

    @Override
    public List<BookCopyResponse> getCopies(UUID bookId) {
        return bookCopyRepository.findByBookId(bookId)
                .stream()
                .map(bookCopyMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteCopy(UUID copyId) {
        if (!bookCopyRepository.existsById(copyId)) {
            throw new RuntimeException("Book copy not found");
        }
        bookCopyRepository.deleteById(copyId);
    }
}
