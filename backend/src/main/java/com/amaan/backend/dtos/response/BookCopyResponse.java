package com.amaan.backend.dtos.response;

import com.amaan.backend.constants.BookStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCopyResponse {
    private UUID id;
    private UUID bookId;
    private BookStatus status;
}
