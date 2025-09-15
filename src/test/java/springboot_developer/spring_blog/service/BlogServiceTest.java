package springboot_developer.spring_blog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot_developer.spring_blog.domain.Article;
import springboot_developer.spring_blog.dto.AddArticleRequest;
import springboot_developer.spring_blog.dto.UpdateArticleRequest;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 책에 나온 내용에 덧붙여, test코드 추가 작성
 */
@SpringBootTest
@Transactional
class BlogServiceTest {

}
