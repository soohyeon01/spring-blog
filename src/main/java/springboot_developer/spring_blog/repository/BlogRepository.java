package springboot_developer.spring_blog.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springboot_developer.spring_blog.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
