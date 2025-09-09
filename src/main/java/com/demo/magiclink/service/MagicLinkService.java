package com.demo.magiclink.service;

import com.demo.magiclink.model.MagicLinkToken;
import com.demo.magiclink.repository.MagicLinkTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MagicLinkService {

    @Autowired
    private MagicLinkTokenRepository tokenRepo;
    private final String baseUrl="http://localhost:8080/magic-link/verify";
    private final int expirationMinutes=15;

    public ResponseEntity<?> requestMagicLink() {
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
        MagicLinkToken token = new MagicLinkToken(expiresAt);
        tokenRepo.save(token);

        String link = baseUrl + "?token=" + token.getToken();
        return new ResponseEntity<>(link,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> verifyToken(String tokenStr) {
        return tokenRepo.findByToken(tokenStr)
                .map(token -> {
                    if (token.isUsed()) return new ResponseEntity<>("Page not available",HttpStatus.BAD_REQUEST);
                    if (token.getExpiresAt().isBefore(LocalDateTime.now())) return new ResponseEntity<>("Token Expired",HttpStatus.BAD_REQUEST);
                    token.setUsed(true);
                    tokenRepo.save(token);
                    return new ResponseEntity<>("Welcome, User",HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

}
