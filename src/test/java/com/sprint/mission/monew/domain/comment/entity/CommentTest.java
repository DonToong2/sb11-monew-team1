package com.sprint.mission.monew.domain.comment.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.domain.article.entity.Article;
import com.sprint.mission.monew.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CommentTest {

    private Article article;
    private User user;
    private String content;
    private Comment comment;

    @BeforeEach
    void setUp() {
        article = new Article();
        user = new User();
        content = "댓글 내용";

        comment = Comment.create(article, user, content);
    }
    @Nested
    @DisplayName("댓글 생성하기")
    class Create {

        @Test
        @DisplayName("댓글 생성")
        void 댓글_생성() {
            // given
            // setUp()의 article, user, content 초기화

            // when
            // setUp()의 comment 초기화

            // then
            assertThat(comment.getArticle()).isEqualTo(article);
            assertThat(comment.getUser()).isEqualTo(user);
            assertThat(comment.getContent()).isEqualTo(content);
        }
    }

    @Nested
    @DisplayName("좋아요")
    class Like {

        @Test
        @DisplayName("좋아요 증가")
        void 좋아요_증가() {
            // given
            // setUp()의 comment 초기화

            // when
            comment.increaseLikeCount();

            // then
            assertThat(comment.getLikeCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("좋아요 감소")
        void 좋아요_감소() {
            // given
            // setUp()의 comment 초기화
            comment.increaseLikeCount(); // 미리 1 증가시키기

            // when
            comment.decreaseLikeCount();

            // then
            assertThat(comment.getLikeCount()).isEqualTo(0);
        }
    }
}
