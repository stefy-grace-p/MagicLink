
import com.example.magiclink.email.EmailSender;
import com.example.magiclink.model.MagicLinkToken;
import com.example.magiclink.repo.MagicLinkTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MagicLinkService {

    private final MagicLinkTokenRepository tokenRepo;
    private final EmailSender emailSender;
    private final String baseUrl;
    private final int expirationMinutes;

    public MagicLinkService(MagicLinkTokenRepository tokenRepo,
                            EmailSender emailSender,
                            @Value("${magiclink.base-url}") String baseUrl,
                            @Value("${magiclink.token-expiration-minutes:15}") int expirationMinutes) {
        this.tokenRepo = tokenRepo;
        this.emailSender = emailSender;
        this.baseUrl = baseUrl;
        this.expirationMinutes = expirationMinutes;
    }

    public void requestMagicLink(String email) {
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
        MagicLinkToken token = new MagicLinkToken(email, expiresAt);
        tokenRepo.save(token);

        String link = baseUrl + "?token=" + token.getToken();
        String subject = "Your magic sign-in link";
        String body = "Hello,\n\nClick the link below to sign in. It will expire at " + token.getExpiresAt() + ".\n\n" + link + "\n\nIf you didn't request this, ignore this email.";
        emailSender.send(email, subject, body);
    }

    @Transactional
    public VerificationResult verifyToken(String tokenStr) {
        return tokenRepo.findByToken(tokenStr)
                .map(t -> {
                    if (t.isUsed()) return new VerificationResult(false, "Token already used");
                    if (t.getExpiresAt().isBefore(LocalDateTime.now())) return new VerificationResult(false, "Token expired");
                    t.setUsed(true);
                    tokenRepo.save(t);
                    // Here you would normally create a session or JWT. For demo, we return success with email.
                    return new VerificationResult(true, "Success", t.getEmail());
                })
                .orElse(new VerificationResult(false, "Invalid token"));
    }

    public static class VerificationResult {
        public final boolean success;
        public final String message;
        public final String email;

        public VerificationResult(boolean success, String message) {
            this(success, message, null);
        }
        public VerificationResult(boolean success, String message, String email) {
            this.success = success;
            this.message = message;
            this.email = email;
        }
    }
}
