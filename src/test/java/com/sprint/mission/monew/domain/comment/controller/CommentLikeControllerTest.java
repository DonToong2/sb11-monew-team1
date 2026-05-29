package com.sprint.mission.monew.domain.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.monew.domain.article.entity.Article;
import com.sprint.mission.monew.domain.article.entity.ArticleSource;
import com.sprint.mission.monew.domain.comment.dto.response.CommentLikeResponse;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import com.sprint.mission.monew.domain.comment.exception.CommentLikeAlreadyExistsException;
import com.sprint.mission.monew.domain.comment.exception.CommentNotFoundException;
import com.sprint.mission.monew.domain.comment.service.CommentLikeService;
import com.sprint.mission.monew.domain.user.entity.User;
import com.sprint.mission.monew.domain.user.exception.UserNotFoundException;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentLikeController.class)
public class CommentLikeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CommentLikeService commentLikeService;

  private Article article;
  private User user;
  private Comment comment;
  private UUID articleId;
  private UUID userId;
  private UUID commentId;
  private UUID commentLikeId;
  private CommentLikeResponse response;

  @BeforeEach
  void setUp() {
    article = Article.create(
        ArticleSource.NAVER,
        "https://example.com/news/1",
        "테스트 기사 제목",
        Instant.parse("2024-01-01T00:00:00Z"),
        "기사 요약 내용"
    );
    user = User.create("Test@naver.com", "test", "12345678");
    comment = Comment.create(article, user, "댓글 내용");

    articleId = article.getId();
    userId = user.getId();
    commentId = comment.getId();
    commentLikeId = UUID.randomUUID();

    response = new CommentLikeResponse(
        commentLikeId,
        userId,
        Instant.now(),
        commentId,
        articleId,
        comment.getUser().getId(),
        comment.getUser().getNickname(),
        comment.getContent(),
        comment.getLikeCount(),
        comment.getCreatedAt()
    );
  }

  @Nested
  @DisplayName("댓글 좋아요 등록하기")
  class Controller_Create_CommentLike {
    
    @Test
    @DisplayName("댓글 좋아요 등록 성공")
    void 댓글_좋아요_등록_성공() throws Exception {
      // given
      // userId, commentId는 BeforeEach에서 초기화
      given(commentLikeService.create(userId, commentId)).willReturn(response);

      // when & then
      mockMvc.perform(post("/api/comments/{commentId}/comment-likes", commentId)
          .header("Monew-Request-User-ID", userId))
          .andExpect(status().isOk());

    }
  }
}
