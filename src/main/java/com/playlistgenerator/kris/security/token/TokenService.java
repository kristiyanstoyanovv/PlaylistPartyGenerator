package com.playlistgenerator.kris.security.token;

import com.playlistgenerator.kris.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<TokenEntity> getToken(String token) {
        return tokenRepository.findByToken(token);
    }
    @Transactional
    public TokenEntity saveToken(TokenEntity token) {
        return tokenRepository.save(token);
    }
    @Transactional
    public int setConfirmedAt(TokenEntity token) {
        return tokenRepository.updateConfirmedAt(
                token.getToken(),LocalDateTime.now());
    }



}
