package com.amaan.backend.dtos.response;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Date publishDate;
    private Integer pages;
    private String previewContent;
    private Integer totalCopies;
    private Integer availableCopies;

}
