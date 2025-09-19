package springboot_developer.spring_blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot_developer.spring_blog.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
