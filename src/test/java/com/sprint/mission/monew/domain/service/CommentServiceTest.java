package com.sprint.mission.monew.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.domain.article.repository.querydsl.impl.ArticleRepository;
import com.sprint.mission.monew.domain.comment.dto.request.CommentCreateRequest;
import com.sprint.mission.monew.domain.comment.dto.response.CommentResponse;
import com.sprint.mission.monew.domain.comment.mapper.CommentMapper;
import com.sprint.mission.monew.domain.comment.repository.CommentRepository;
import com.sprint.mission.monew.domain.comment.service.CommentService;
import com.sprint.mission.monew.domain.user.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @Nested
    @DisplayName("댓글 등록하기")
    class 댓글_등록하기 {

        @Test
        @DisplayName("댓글 등록")
        void 댓글_등록() {
            // given
            UUID articleId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();

            CommentCreateRequest request = new CommentCreateRequest(articleId, userId, "댓글 내용");

            // when
            CommentResponse response = commentService.create(request);

            // then
            assertThat(response).isNotNull();

        }
    }

}
