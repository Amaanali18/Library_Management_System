package com.amaan.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens" , uniqueConstraints = @UniqueConstraint(columnNames = "tokens"))
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @NotNull
    @Column(nullable = false, unique = true, length = 512)
    private String token;
    @CreationTimestamp
    private Instant createdAt;
    @Column(nullable = false, updatable = false)
    private Instant expiresAt;
    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;

    public boolean getRevoked() {
        return revoked;
    }
}
