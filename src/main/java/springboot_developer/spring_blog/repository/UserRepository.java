package springboot_developer.spring_blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot_developer.spring_blog.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // 이메일로 사용자 정보를 가져옴(즉, 이메일이 이름이 되는 것)
}
