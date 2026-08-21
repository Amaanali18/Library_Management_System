package com.amaan.backend.dtos.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Date publishDate;
    private Integer pages;
    private String content;
    private String previewContent;
}
