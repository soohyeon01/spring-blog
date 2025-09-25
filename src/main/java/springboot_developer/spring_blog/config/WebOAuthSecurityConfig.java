package springboot_developer.spring_blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springboot_developer.spring_blog.config.jwt.TokenProvider;
import springboot_developer.spring_blog.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import springboot_developer.spring_blog.config.oauth.OAuth2SuccessHandler;
import springboot_developer.spring_blog.config.oauth.OAuth2UserCustomService;
import springboot_developer.spring_blog.repository.RefreshTokenRepository;
import springboot_developer.spring_blog.service.UserService;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() {  // 스프링 시큐리티 기능 비활성화
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 토큰 방식 인증을 위해 기존 사용하던 폼로그인, 세션 비활성화
        http.csrf(AbstractHttpConfigurer::disable)  
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);
        
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        // 헤더를 확인할 커스텀 필터 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 토큰 재발급 URL 은 인증 없이 접근 가능하도록 설정, 나머지 API URL 은 인증 필요
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/token").permitAll()  // 인증 없이 접근
                .requestMatchers("/api/**").authenticated() // 인증 필요
                .anyRequest().permitAll()
        );

        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                // Authorization 요청과 관련된 상태 저장
                .authorizationEndpoint(authorization ->
                        authorization.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                )
                // 인증 성공 시 실행할 핸들러
                .successHandler(oAuth2SuccessHandler())
                .userInfoEndpoint(userInfo ->
                        userInfo.userService(oAuth2UserCustomService)
                )
        );

        http.logout(logout ->
                logout.logoutSuccessUrl("/login")
        );

        // /api 로 시작하는 url 인 경우 401 상태(UNAUTHORIZED) 코드를 반환하도록 예외 처리
        http.exceptionHandling(exceptions ->
                exceptions.defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        request -> request.getRequestURI().startsWith("/api/")   // 직접 람다식으로 매칭
                )
        );
        return http.build();
    }


    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(
                tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}