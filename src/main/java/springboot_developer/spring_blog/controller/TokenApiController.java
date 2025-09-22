package springboot_developer.spring_blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot_developer.spring_blog.dto.CreateAccessTokenRequest;
import springboot_developer.spring_blog.dto.CreateAccessTokenResponse;
import springboot_developer.spring_blog.service.TokenService;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    // Post 요청이 오면 토큰 서비스에서 리프레시 토큰을 기반으로 새로운 액세스 토큰을 만들어주는 api
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createAccessToken
            (@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
