package springboot_developer.spring_blog;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springboot_developer.spring_blog.domain.Article;

import java.time.LocalDateTime;

/**
 * 글 3개 미리 넣어두기
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
        initService.dbInit3();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Article article = createArticle("제목 1", "내용 1", LocalDateTime.now(), LocalDateTime.now());
            em.persist(article);
        }

        public void dbInit2() {
            Article article = createArticle("제목 2", "내용 2", LocalDateTime.now(), LocalDateTime.now());
            em.persist(article);
        }

        public void dbInit3() {
            Article article = createArticle("제목 3", "내용 3", LocalDateTime.now(), LocalDateTime.now());
            em.persist(article);
        }

        private Article createArticle(String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
            return new Article(title, content, createdAt, updatedAt);
        }

    }
}

