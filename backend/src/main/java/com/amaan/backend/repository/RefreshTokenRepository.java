package com.amaan.backend.repository;

import com.amaan.backend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Query("""
    SELECT rt
    FROM RefreshToken rt
    JOIN FETCH rt.user u
    JOIN FETCH u.role
    WHERE rt.token = :token
    """)
    Optional<RefreshToken> findByToken(@Param("token") String token);
}
