package springboot_developer.spring_blog.confing.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import springboot_developer.spring_blog.config.jwt.JwtProperties;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.*;

@Getter
public class JwtFactory {

    private final SecretKey jwtSigningKey;  // 의존성 주입
    private final JwtProperties jwtProperties;


    private String subject = "test@email.com";
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = emptyMap();

    /* 빌더 패턴을 사용해 설정이 필요한 데이터만 선택 설정 */
    @Builder
    public JwtFactory(SecretKey jwtSigningKey, JwtProperties jwtProperties, String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
        this.jwtSigningKey = jwtSigningKey;
        this.jwtProperties = jwtProperties;
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    public static JwtFactory withDefaultValues(SecretKey jwtSigningKey, JwtProperties jwtProperties) {
        return JwtFactory.builder()
                .jwtSigningKey(jwtSigningKey)
                .jwtProperties(jwtProperties)
                .build();
    }

//    public static JwtFactory withDefaultValues() {
//        return JwtFactory.builder().build();
//    }  위의 withDefaultValues 로 변경

    /* jjwt 라이브러리를 사용해 JWT 토큰 생성 */
    public String createToken() {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(jwtSigningKey, SignatureAlgorithm.HS256)   // 서명: 비밀값과 함께 해시값을 HS256 방식으로 암호화(새로운 버전 방식 - 수정)
                .compact();
    }
}
