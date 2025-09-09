package com.demo.magiclink.model;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "magic_link_tokens")
public class MagicLinkToken {
    @Id
    private String id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String token;

    @NotBlank(message = "Must not be null or blank")
    private LocalDateTime expiresAt;

    @NotBlank(message = "Must not be null or blank")
    private boolean used = false;

    public MagicLinkToken() {}

    public MagicLinkToken(String email, LocalDateTime expiresAt) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.token = UUID.randomUUID().toString();
        this.expiresAt = expiresAt;
        this.used = false;
    }

    // getters and setters

    public String getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
}
