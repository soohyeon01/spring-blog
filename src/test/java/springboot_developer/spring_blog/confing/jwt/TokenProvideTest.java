package springboot_developer.spring_blog.confing.jwt;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import springboot_developer.spring_blog.config.jwt.JwtProperties;
import springboot_developer.spring_blog.config.jwt.TokenProvider;
import springboot_developer.spring_blog.domain.User;
import springboot_developer.spring_blog.repository.UserRepository;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProvideTest {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private SecretKey jwtSigningKey;  // JwtConfig에서 주입 - 수정

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    public void generateToken() {
        //given: 토큰에 유저정보를 추가하기 위한 테스트 유저 생성
        User testUser = userRepository.save(User.builder()
                .email("user@email.com")
                .password("test")
                .build());

        System.out.println("SecretKey=" + jwtProperties.getSecretKey());  // null 체크 - 추가

        //when: 메서드를 호출해 토큰 생성
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        //then: jjwt 라이브러리를 사용해 토큰 복호화. 토큰 생성시 클레임으로 넣어둔 id 값이 테스트 유저와 동일한지 확인
        Long userId = Jwts.parserBuilder()
                .setSigningKey(jwtSigningKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        // given: 이미 만료된 토큰 생성
        String token = JwtFactory.builder()
                .jwtSigningKey(jwtSigningKey)
                .jwtProperties(jwtProperties)
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken();

        // when: 메서드를 호출하여 유효한 토큰인지 검증
        boolean result = tokenProvider.validToken(token);

        // then: 반환값이 유효하지 않다는 것을 확인
        assertThat(result).isFalse();
    }


    @DisplayName("validToken(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken() {
        // given: 만료되지 않은 토큰 생성
        String token = JwtFactory.withDefaultValues(jwtSigningKey, jwtProperties)
                .createToken();

        // when:
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }


    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // given:
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .jwtSigningKey(jwtSigningKey)   // 반드시 주입
                .jwtProperties(jwtProperties)   // 반드시 주입
                .subject(userEmail)
                .build()
                .createToken();

        // when: 인증 객체 반환
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .jwtSigningKey(jwtSigningKey)   // 반드시 주입
                .jwtProperties(jwtProperties)   // 반드시 주입
                .claims(Map.of("id", userId))
                .build()
                .createToken();

        // when
        Long userIdByToken = tokenProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }

}
