package springboot_developer.spring_blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springboot_developer.spring_blog.domain.User;
import springboot_developer.spring_blog.dto.AddUserRequest;
import springboot_developer.spring_blog.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // BCryptPasswordEncoder 를 생성자를 사용해 직접 생성해서 패스워드를 암호화할 수 있게 코드를 수정함
    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    // 전달받은 유저 ID로 유저를 검색해서 전달하는 메서드 추가 - 토큰 서비스
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected user"));
    }
    
    // 이메일을 입력받아 users 테이블에서 유저를 찾고, 없으면 예외 발생
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Unexpected user"));
    }
}
