package com.sprint.mission.monew.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.monew.domain.article.entity.Article;
import com.sprint.mission.monew.domain.article.entity.ArticleSource;
import com.sprint.mission.monew.domain.comment.dto.response.CommentLikeResponse;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import com.sprint.mission.monew.domain.comment.entity.CommentLike;
import com.sprint.mission.monew.domain.comment.mapper.CommentLikeMapper;
import com.sprint.mission.monew.domain.comment.repository.CommentLikeRepository;
import com.sprint.mission.monew.domain.comment.repository.CommentRepository;
import com.sprint.mission.monew.domain.user.entity.User;
import com.sprint.mission.monew.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentLikeServiceTest {

  @InjectMocks
  private CommentLikeService commentLikeService;

  @Mock
  private CommentLikeRepository commentLikeRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private CommentLikeMapper commentLikeMapper;

  private UUID articleId;
  private UUID userId;
  private UUID commentId;
  private UUID commentLikeId;
  private Article article;
  private User user;
  private Comment comment;

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
  }

  @Nested
  @DisplayName("댓글 좋아요 등록하기")
  class Service_CommentLike_Create {

    @Test
    @DisplayName("댓글 좋아요 등록 성공")
    void 댓글좋아요_등록_성공() {
      // given
      // comment(commentId, userId), user는 BeforeEach에서 초기화

      // 여기서는 자신의 댓글에 좋아요를 누른거로 테스트
      CommentLikeResponse expectedResponse = new CommentLikeResponse(
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

      given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
      given(userRepository.findById(userId)).willReturn(Optional.of(user));
      given(commentLikeRepository.save(any(CommentLike.class)))
          .willAnswer(invocation -> invocation.getArgument(0));
      given(commentLikeMapper.toResponse(any(CommentLike.class))).willReturn(expectedResponse);

      // when
      CommentLikeResponse response = commentLikeService.create(commentId, userId);

      // then
      assertThat(response).isNotNull();
      assertThat(response).isEqualTo(expectedResponse);
      assertThat(comment.getLikeCount()).isEqualTo(1);
      verify(commentLikeRepository).save(any(CommentLike.class));
      verify(commentLikeMapper).toResponse(any(CommentLike.class));
    }
  }

}
