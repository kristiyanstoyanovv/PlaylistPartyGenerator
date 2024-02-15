package com.playlistgenerator.kris.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE TokenEntity t SET t.confirmedAt = ?2 WHERE t.tokenId = ?1")
    int updateConfirmedAt(Long tokenId, LocalDateTime confirmedAt);
}
