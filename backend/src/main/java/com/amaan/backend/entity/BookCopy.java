package com.amaan.backend.entity;

import com.amaan.backend.constants.BookStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_copy_id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BookStatus status = BookStatus.AVAILABLE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id" , nullable = false)
    private Book book;
}
