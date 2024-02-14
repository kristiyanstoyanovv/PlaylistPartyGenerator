package com.playlistgenerator.kris.security.token;

import com.playlistgenerator.kris.user.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class TokenEntity {
    @Id
    @SequenceGenerator(name = "token_sequence", sequenceName = "token_sequence")
    @GeneratedValue(generator = "token_sequence", strategy = GenerationType.SEQUENCE)
    private Long tokenId;
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private UserEntity user;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime confirmedAt;

    public TokenEntity() {
    }

    public TokenEntity(UserEntity user, String token, LocalDateTime createdAt) {
        this.user = user;
        this.token = token;
        this.createdAt = createdAt;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    @Override
    public String toString() {
        return "TokenEntity{" +
                "tokenId=" + tokenId +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
