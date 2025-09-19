package springboot_developer.spring_blog.config.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    private final JwtProperties jwtProperties;

    public JwtConfig(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public SecretKey jwtSigningKey() {
        String secret = jwtProperties.getSecretKey();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("jwt.secret-key 값이 설정되지 않았습니다.");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }     // 바이트 배열로 변환한 뒤 SecretKey로 감싸야 함 - 수정
}
