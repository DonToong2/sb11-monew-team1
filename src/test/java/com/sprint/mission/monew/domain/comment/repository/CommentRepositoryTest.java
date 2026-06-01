package com.sprint.mission.monew.domain.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.common.config.JpaConfig;
import com.sprint.mission.monew.common.config.QuerydslConfig;
import com.sprint.mission.monew.common.dto.SortDirection;
import com.sprint.mission.monew.domain.article.entity.Article;
import com.sprint.mission.monew.domain.article.entity.ArticleSource;
import com.sprint.mission.monew.domain.article.repository.ArticleRepository;
import com.sprint.mission.monew.domain.comment.dto.CommentOrderBy;
import com.sprint.mission.monew.domain.comment.dto.CommentQueryCondition;
import com.sprint.mission.monew.domain.comment.dto.CommentResponse;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import com.sprint.mission.monew.domain.user.entity.User;
import com.sprint.mission.monew.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, QuerydslConfig.class})
public class CommentRepositoryTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private Article article;
  private User user;
  private Comment comment;

  private Instant baseTime;

  @BeforeEach
  void setUp() {
    article = articleRepository.save(
        Article.create(
            ArticleSource.NAVER,
            "https://example.com/news/1",
            "테스트 기사 제목",
            Instant.parse("2024-01-01T00:00:00Z"),
            "기사 요약 내용"
        ));
    user = userRepository.save(User.create(
        "Test@naver.com", "test", "12345678"
    ));
    comment = Comment.create(article, user, "댓글 내용");

    baseTime = Instant.parse("2024-01-01T00:00:00Z");
  }

  @Nested
  @DisplayName("save() 테스트")
  class Save {

    @Test
    @DisplayName("댓글 저장 성공")
    void 댓글_저장_성공() {
      // given
      // comment는 BeforeEach에서 초기화

      // when
      Comment savedComment = commentRepository.save(comment);

      // then
      assertThat(savedComment.getId()).isNotNull();
      assertThat(savedComment.getArticle().getId()).isEqualTo(article.getId());
      assertThat(savedComment.getUser().getId()).isEqualTo(user.getId());
      assertThat(savedComment.getContent()).isEqualTo("댓글 내용");
    }
  }

  @Nested
  @DisplayName("findById() 테스트")
  class FindById {

    @Test
    @DisplayName("존재하지 않는 댓글 조회")
    void 존재하지_않는_댓글_조회() {
      // given
      UUID notSavedCommentId = UUID.randomUUID();

      // when
      Optional<Comment> foundComment = commentRepository.findById(notSavedCommentId);

      // then
      assertThat(foundComment).isEmpty();
    }

    @Test
    @DisplayName("댓글 조회 성공")
    void 댓글_조회_성공() {
      // given
      Comment savedComment = commentRepository.save(comment);

      // when
      Comment foundComment = commentRepository.findById(savedComment.getId()).orElseThrow();

      // then
      assertThat(foundComment.getId()).isEqualTo(savedComment.getId());
      assertThat(foundComment.getArticle().getId()).isEqualTo(savedComment.getArticle().getId());
      assertThat(foundComment.getUser().getId()).isEqualTo(savedComment.getUser().getId());
      assertThat(foundComment.getContent()).isEqualTo(savedComment.getContent());
    }

  }

  @Nested
  @DisplayName("delete() 테스트")
  class Delete {

    @Test
    @DisplayName("댓글 삭제 성공")
    void 댓글_삭제_성공() {
      // given
      Comment savedComment = commentRepository.save(comment);
      commentRepository.delete(savedComment);

      // when
      Optional<Comment> result = commentRepository.findById(comment.getId());

      // then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("increaseLikeCount() 테스트")
  class IncreaseLikeCount {

    @Test
    @DisplayName("댓글 좋아요 +1 증가 성공")
    void 댓글_좋아요_1_증가_성공() {
      // given
      Comment savedComment = commentRepository.save(comment);
      Comment before = commentRepository.findById(savedComment.getId()).orElseThrow();
      assertThat(before.getLikeCount()).isEqualTo(0);

      // when
      commentRepository.increaseLikeCount(comment.getId());

      testEntityManager.flush();
      testEntityManager.clear();

      // then
      Comment after = commentRepository.findById(savedComment.getId()).orElseThrow();
      assertThat(after.getLikeCount()).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("decreaseLikeCount() 테스트")
  class DecreaseLikeCount {

    @Test
    @DisplayName("댓글 좋아요 취소 성공")
    void 댓글_좋아요_취소_성공() {
      // given
      Comment savedComment = commentRepository.save(comment);

      commentRepository.findById(savedComment.getId()).orElseThrow();
      commentRepository.increaseLikeCount(savedComment.getId());
      testEntityManager.flush();
      testEntityManager.clear();
      Comment before = commentRepository.findById(savedComment.getId()).orElseThrow();
      assertThat(before.getLikeCount()).isEqualTo(1);

      // when
      commentRepository.decreaseLikeCount(savedComment.getId());

      testEntityManager.flush();
      testEntityManager.clear();

      // then
      Comment after = commentRepository.findById(savedComment.getId()).orElseThrow();
      assertThat(after.getLikeCount()).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("댓글 목록 조회하기")
  class Find {

    @Test
    @DisplayName("등록순(createdAt DESC) 조회")
    void 등록순_조회() {
      // given
      UUID articleId = article.getId();

      Comment firstComment = commentRepository.save(Comment.create(article, user, "첫 번째 댓글"));

      Comment secondComment = commentRepository.save(Comment.create(article, user, "두 번째 댓글"));

      testEntityManager.flush();
      testEntityManager.clear();

      firstComment = commentRepository.findById(firstComment.getId()).orElseThrow();
      secondComment = commentRepository.findById(secondComment.getId()).orElseThrow();

      CommentQueryCondition condition = new CommentQueryCondition(
          articleId,
          CommentOrderBy.CREATED_AT,
          SortDirection.DESC,
          null,
          null,
          5
      );

      // when
      List<CommentResponse> comments = commentRepository.getComments(condition, user.getId());

      // then
      assertThat(comments).hasSize(2);
      assertThat(comments).extracting(CommentResponse::createdAt)
          .isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("좋아요순(likeCount DESC), 2순위 등록순(createdAt DESC) 조회")
    void 좋아요순_등록순_조회() {
      // given
      UUID articleId = article.getId();

      Comment firstComment = commentRepository.save(Comment.create(article, user, "첫 번째 댓글"));

      Comment secondComment = commentRepository.save(Comment.create(article, user, "두 번째 댓글"));

      Comment thirdComment = commentRepository.save(Comment.create(article, user, "세 번째 댓글"));

      // 첫 번째 댓글 : 좋아요 2개
      commentRepository.increaseLikeCount(firstComment.getId());
      commentRepository.increaseLikeCount(firstComment.getId());

      // 두 번째 댓글 : 좋아요 2개
      commentRepository.increaseLikeCount(secondComment.getId());
      commentRepository.increaseLikeCount(secondComment.getId());

      // 세 번째 댓글 : 좋아요 1개
      commentRepository.increaseLikeCount(thirdComment.getId());

      testEntityManager.flush();
      testEntityManager.clear();

      firstComment = commentRepository.findById(firstComment.getId()).orElseThrow();
      secondComment = commentRepository.findById(secondComment.getId()).orElseThrow();
      thirdComment = commentRepository.findById(thirdComment.getId()).orElseThrow();

      CommentQueryCondition condition = new CommentQueryCondition(
          articleId,
          CommentOrderBy.LIKE_COUNT,
          SortDirection.DESC,
          null,
          null,
          5
      );

      // when
      List<CommentResponse> comments = commentRepository.getComments(condition, user.getId());

      // then
      assertThat(comments).hasSize(3);

      // 2번째(좋아요2개, 등록순 2번째), 1번째(좋아요 2개, 등록순 1번째), 3번째(좋아요 1개) 순으로 정렬되어야 함
      assertThat(comments.get(0).id()).isEqualTo(secondComment.getId());
      assertThat(comments.get(1).id()).isEqualTo(firstComment.getId());
      assertThat(comments.get(2).id()).isEqualTo(thirdComment.getId());
    }

    @Test
    @DisplayName("등록순 첫 페이지 조회")
    void 등록순_조회_cursor_null() {
      // given
      Comment firstComment = commentRepository.save(Comment.create(article, user, "첫 번째 댓글"));

      Comment secondComment = commentRepository.save(Comment.create(article, user, "두 번째 댓글"));

      testEntityManager.flush();
      testEntityManager.clear();

      CommentQueryCondition condition = new CommentQueryCondition(
          article.getId(),
          CommentOrderBy.CREATED_AT,
          SortDirection.DESC,
          null,
          null,
          5
      );

      // when
      List<CommentResponse> comments = commentRepository.getComments(condition, user.getId());

      // then
      assertThat(comments).hasSize(2);
    }

    @Test
    @DisplayName("등록순 다음 페이지 조회")
    void 등록순_조회_cursor() {
      // given
      Comment firstComment = commentRepository.save(Comment.create(article, user, "첫 번째 댓글"));

      Comment secondComment = commentRepository.save(Comment.create(article, user, "두 번째 댓글"));

      Comment thirdComment = commentRepository.save(Comment.create(article, user, "세 번째 댓글"));

      testEntityManager.flush();
      testEntityManager.clear();

      firstComment = commentRepository.findById(firstComment.getId()).orElseThrow();
      secondComment = commentRepository.findById(secondComment.getId()).orElseThrow();
      thirdComment = commentRepository.findById(thirdComment.getId()).orElseThrow();

      CommentQueryCondition condition = new CommentQueryCondition(
          article.getId(),
          CommentOrderBy.CREATED_AT,
          SortDirection.DESC,
          secondComment.getCreatedAt().toString(), // 두번째 시간 이전의 댓글(firstComment)만 조회됨
          null,
          5
      );

      // when
      List<CommentResponse> comments = commentRepository.getComments(condition, user.getId());

      // then
      assertThat(comments).hasSize(1); // 그래서 size는 3이 아닌 1이 나옴
      assertThat(comments.get(0).id()).isEqualTo(firstComment.getId());
    }

    @Test
    @DisplayName("좋아요순(2순위 등록순) 커서 조회")
    void 좋아요순_등록순_조회_cursor() {
      // given
      UUID articleId = article.getId();

      Comment firstComment = commentRepository.save(Comment.create(article, user, "첫 번째 댓글"));

      Comment secondComment = commentRepository.save(Comment.create(article, user, "두 번째 댓글"));

      Comment thirdComment = commentRepository.save(Comment.create(article, user, "세 번째 댓글"));

      testEntityManager.flush();
      testEntityManager.clear();

      firstComment = commentRepository.findById(firstComment.getId()).orElseThrow();
      secondComment = commentRepository.findById(secondComment.getId()).orElseThrow();
      thirdComment = commentRepository.findById(thirdComment.getId()).orElseThrow();

      // 첫 번째 댓글 : 좋아요 2개
      commentRepository.increaseLikeCount(firstComment.getId());
      commentRepository.increaseLikeCount(firstComment.getId());

      // 두 번째 댓글 : 좋아요 2개
      commentRepository.increaseLikeCount(secondComment.getId());
      commentRepository.increaseLikeCount(secondComment.getId());

      // 세 번째 댓글 : 좋아요 1개
      commentRepository.increaseLikeCount(thirdComment.getId());

      CommentQueryCondition condition = new CommentQueryCondition(
          articleId,
          CommentOrderBy.LIKE_COUNT,
          SortDirection.DESC,
          "2",
          secondComment.getCreatedAt(),
          5
      );

      // when
      List<CommentResponse> comments = commentRepository.getComments(condition, user.getId());

      // then
      // 첫번째 페이지(2번째, 1번째 댓글) 이후 페이지에 3번째 댓글이 나와야 함
      assertThat(comments).hasSize(1);
      assertThat(comments.get(0).id()).isEqualTo(thirdComment.getId());
    }

    @Test
    @DisplayName("기사별 댓글 수 조회")
    void 기사별_댓글_수_조회() {
      // given
      Article anotherArticle = articleRepository.save(Article.create(
          ArticleSource.NAVER,
          "https://example.com/news/2",
          "테스트 기사 제목2",
          Instant.parse("2024-01-02T00:00:00Z"),
          "기사 요약 내용"
      ));

      commentRepository.save(Comment.create(article, user, "기사1 첫 번째 댓글"));
      commentRepository.save(Comment.create(article, user, "기사1 두 번째 댓글"));
      commentRepository.save(Comment.create(anotherArticle, user, "기사2 첫 번째 댓글"));
      commentRepository.save(Comment.create(article, user, "기사1 세 번째 댓글"));

      testEntityManager.flush();
      testEntityManager.clear();

      // when
      long count = commentRepository.countByArticleId(article.getId());
      long count2 = commentRepository.countByArticleId(anotherArticle.getId());
      // then
      assertThat(count).isEqualTo(3);
      assertThat(count2).isEqualTo(1);
    }

  }
}
