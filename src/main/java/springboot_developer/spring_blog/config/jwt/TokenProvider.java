package springboot_developer.spring_blog.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import springboot_developer.spring_blog.domain.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final SecretKey jwtSigningKey;  // 의존성 주입
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    /* JWT 토큰 생성 메서드 */
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   // 헤더 typ : JWT
                .setIssuer(jwtProperties.getIssuer())   // 내용 iss : properties 파일에서 설정한 값
                .setIssuedAt(now)   // 내용 iat : 현재 시간
                .setExpiration(expiry)  // 내용 exp : expiry 멤버 변숫값
                .setSubject(user.getEmail())    // 내용 sub : 유저의 이메일
                .claim("id", user.getId())  // 클레임id: 유저ID
                .signWith(jwtSigningKey, SignatureAlgorithm.HS256)   // 서명: 비밀값과 함께 해시값을 HS256 방식으로 암호화(새로운 버전 방식 - 수정)
                .compact();
    }

    /* JWT 토큰 유효성 검증 메서드 */
    public boolean validToken(String token) {
        try {
            getParser().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* 토큰 기반으로 인증 정보를 가져오는 메서드 */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject(), "", authorities), token, authorities);
    }

    /* 토큰 기반으로 유저 ID를 가져오는 메소드 */
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return getParser()
                .parseClaimsJws(token)
                .getBody();
    }

    private JwtParser getParser() {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSigningKey)   // SecretKey 사용
                .build();
    }

}
