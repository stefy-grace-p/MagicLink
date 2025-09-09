import com.example.magiclink.service.MagicLinkService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/magic-link")
@Validated
public class MagicLinkController {

    private final MagicLinkService magicLinkService;

    public MagicLinkController(MagicLinkService magicLinkService) {
        this.magicLinkService = magicLinkService;
    }

    public static class RequestDto {
        @Email
        @NotBlank
        public String email;
    }

    @PostMapping("/request")
    public ResponseEntity<?> request(@RequestBody RequestDto dto) {
        // In production you may want to rate-limit, always respond with 200 to avoid email enumeration, etc.
        magicLinkService.requestMagicLink(dto.email);
        return ResponseEntity.ok().body("If that email exists, a magic link was sent.");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("token") String token) {
        var r = magicLinkService.verifyToken(token);
        if (r.success) {
            // Return a session token or redirect to frontend with a JWT. Here we just return the email.
            return ResponseEntity.ok().body("Verified. Signed in as: " + r.email);
        } else {
            return ResponseEntity.badRequest().body(r.message);
        }
    }
}