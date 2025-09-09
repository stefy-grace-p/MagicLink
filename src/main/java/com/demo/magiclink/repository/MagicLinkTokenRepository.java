import com.example.magiclink.model.MagicLinkToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MagicLinkTokenRepository extends JpaRepository<MagicLinkToken, String> {
    Optional<MagicLinkToken> findByToken(String token);
}
