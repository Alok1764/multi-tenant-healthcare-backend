package com.healthcare.repository;

import com.healthcare.model.RefreshToken;
import com.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

    List<RefreshToken> findByUserId(Long userId);

    // Find valid (non-revoked, non-expired) token
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.token = :token " +
           "AND rt.isRevoked = false AND rt.expiresAt > :now")
    Optional<RefreshToken> findValidToken(@Param("token") String token, 
                                          @Param("now") LocalDateTime now);

    // Find all valid tokens for a user
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user.id = :userId " +
           "AND rt.isRevoked = false AND rt.expiresAt > :now")
    List<RefreshToken> findValidTokensByUserId(@Param("userId") Long userId, 
                                                @Param("now") LocalDateTime now);

    // Delete expired tokens
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    // Revoke all tokens for a user
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.user.id = :userId")
    void revokeAllUserTokens(@Param("userId") Long userId);

    // Delete all revoked tokens
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.isRevoked = true")
    void deleteRevokedTokens();
}
