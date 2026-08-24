package com.amaan.backend.mappers;

import com.amaan.backend.dtos.response.BookCopyResponse;
import com.amaan.backend.entity.BookCopy;
import org.springframework.stereotype.Component;

@Component
public class BookCopyMapper {
    public BookCopyResponse toResponse(BookCopy bookCopy) {
        return BookCopyResponse.builder()
                .id(bookCopy.getId())
                .bookId(bookCopy.getBook().getId())
                .status(bookCopy.getStatus())
                .build();
    }
}
