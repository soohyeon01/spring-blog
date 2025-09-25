package springboot_developer.spring_blog.config.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import springboot_developer.spring_blog.domain.User;
import springboot_developer.spring_blog.repository.UserRepository;

import java.util.Map;

/**
 * 리소스 서버에서 보내주는 사용자 정보를 불러오는 메서드인 loadUser()를 통해 사용자를 조회하고,
 * users 테이블에 사용자 정보가 있다면 이름을 업데이트하고 없다면 saveOrUpdate() 메서드를 실행해 users 테이블에 회원 데이터 추가
 */

@RequiredArgsConstructor
@Service
// 부모 클래스인 DefaultOAuth2UserService 에서 OAuth 서비스 제공
// OAuth 서비스에서 제공하는 정보를 기반으로 유저객체를 만들어주는 loadUser() 메서드 사용
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 요청을 바탕으로 유저 정보를 담은 객체 반환
        // 사용자 객체는 식별자, 이름, 이메일, 프로필 사진 링크 등의 정보를 담고 있음
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    // 유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name)) // user 테이블에 있으면 업데이트
                .orElse(User.builder() // 없으면 사용자 새로 생성          
                        .email(email)
                        .nickname(name)
                        .build());
        return userRepository.save(user);
    }

}
