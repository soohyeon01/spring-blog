package springboot_developer.spring_blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot_developer.spring_blog.domain.Article;
import springboot_developer.spring_blog.dto.AddArticleRequest;
import springboot_developer.spring_blog.dto.UpdateArticleRequest;
import springboot_developer.spring_blog.repository.BlogRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    /**
     * 글을 수정하거나 삭제할 때 요청 헤더에 토큰을 전달하므로 사용자 자신이 작성한 글인지 검증 가능
     * 수정, 삭제를 시도하는 경우, 본인 글이 아니면 예외를 발생시키도록 수정
     */
    public void delete(Long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));  // id를 찾지 못할 경우 예외

        authorizeArticleAuthor(article);    // 본인 글이 아니면 예외
        blogRepository.delete(article);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);    // 본인 글이 아니면 예외
        article.update(request.getTitle(), request.getContent());
        return article;
    }

    // 게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

}
