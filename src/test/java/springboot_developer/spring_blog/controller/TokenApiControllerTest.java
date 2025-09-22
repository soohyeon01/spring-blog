package springboot_developer.spring_blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springboot_developer.spring_blog.config.jwt.JwtProperties;
import springboot_developer.spring_blog.confing.jwt.JwtFactory;
import springboot_developer.spring_blog.domain.RefreshToken;
import springboot_developer.spring_blog.domain.User;
import springboot_developer.spring_blog.dto.CreateAccessTokenRequest;
import springboot_developer.spring_blog.repository.RefreshTokenRepository;
import springboot_developer.spring_blog.repository.UserRepository;

import javax.crypto.SecretKey;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private SecretKey jwtSigningKey;

    @BeforeEach
    public void mockMvxSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        userRepository.deleteAll();
    }

    /**
     * 토큰을 생성하는 메서드인 createNewAccessToken() 메서드를 검증
     */
    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        //given: 테스트 유저를 생성하고, 리프레시 토큰을 만들어 데이터베이스에 저장
        final String url = "/api/token";

        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        String refreshToken = JwtFactory.builder()
                .jwtSigningKey(jwtSigningKey)
                .jwtProperties(jwtProperties)
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken();

        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);

        final String requestBody = objectMapper.writeValueAsString(request);

        //when: 토큰 추가 API를 보냄. 요청 타입 JSON, given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보냄
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then: 응답코드가 201인지 확인하고 응답으로 온 액세스 토큰이 비어 있지 않은지 확인
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

}
