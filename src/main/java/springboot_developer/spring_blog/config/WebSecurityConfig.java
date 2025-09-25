//package springboot_developer.spring_blog.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import springboot_developer.spring_blog.service.UserDetailService;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//

/**
 * OAuth2 와 JWT를 사용하려면 기존 폼 로그인 방식을 지우고 새로 설정해야함  
 */

//@RequiredArgsConstructor
//@Configuration
//public class WebSecurityConfig {
//
//    private final UserDetailService userService;
//
//    /* 스프링 시큐리티 기능 비활성화 */
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
//                .requestMatchers("/static/**");
//    }
//
//    /* 특정 HTTP 요청에 대한 웹 기반 보안 구성 */
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/login", "/signup", "/user").permitAll()
//                        .anyRequest().authenticated()
//                )  // spring security 6.1 버전 이상에서 람다 블럭 안에서 선언하는 방식으로 교체됨
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/articles").permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/login").permitAll()
//                        .invalidateHttpSession(true)
//                )
//                .csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//
//        daoAuthenticationProvider.setUserDetailsService(userService);
//        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
//
//        return daoAuthenticationProvider;
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}