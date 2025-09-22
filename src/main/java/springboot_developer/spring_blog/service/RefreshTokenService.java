package springboot_developer.spring_blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot_developer.spring_blog.domain.RefreshToken;
import springboot_developer.spring_blog.repository.RefreshTokenRepository;



@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 전달받은 리프레시 토큰으로 리프레시 토큰 객체를 검색해서 전달하는 메서드 구현
     */
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
