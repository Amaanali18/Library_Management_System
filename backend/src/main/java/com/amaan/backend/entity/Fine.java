package com.amaan.backend.entity;

import com.amaan.backend.constants.FineStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrow_record_id", nullable = false, unique = true)
    private BorrowRecord borrowRecord;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private long daysOverdue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FineStatus status = FineStatus.UNPAID;

    @CreationTimestamp
    private Instant createdAt;

    private Instant paidAt;
}