package com.demo.magiclink.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "magic_link_tokens")
public class MagicLinkToken {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    public MagicLinkToken() {}

    public MagicLinkToken(LocalDateTime expiresAt) {
        this.id = UUID.randomUUID().toString();
        this.token = UUID.randomUUID().toString();
        this.expiresAt = expiresAt;
        this.used = false;
    }

    // getters and setters

    public String getId() { return id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
}
