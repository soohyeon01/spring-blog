package springboot_developer.spring_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing	// 글 등록, 수정 시간 자동 업데이트
@SpringBootApplication
public class SpringBlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBlogApplication.class, args);
	}
}
