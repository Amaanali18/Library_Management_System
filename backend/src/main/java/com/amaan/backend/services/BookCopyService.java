package com.amaan.backend.services;

import com.amaan.backend.dtos.response.BookCopyResponse;

import java.util.List;
import java.util.UUID;

public interface BookCopyService {

    BookCopyResponse addCopy(UUID bookId);

    List<BookCopyResponse> getCopies(UUID bookId);

    void deleteCopy(UUID copyId);
}
