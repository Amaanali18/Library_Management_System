package com.amaan.backend.mappers;

import com.amaan.backend.dtos.request.BookRequest;
import com.amaan.backend.dtos.response.BookResponse;
import com.amaan.backend.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .publishDate(request.getPublishDate())
                .pages(request.getPages())
                .content(request.getContent())
                .previewContent(request.getPreviewContent())
                .build();
    }

    public BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .publishDate(book.getPublishDate())
                .pages(book.getPages())
                .previewContent(book.getPreviewContent())
                .totalCopies(0)
                .availableCopies(0)
                .build();
    }
}
