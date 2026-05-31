package com.sprint.mission.monew.domain.comment.repository.querydsl.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.monew.domain.comment.dto.request.CommentQueryCondition;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import com.sprint.mission.monew.domain.comment.entity.QComment;
import com.sprint.mission.monew.domain.comment.repository.querydsl.CommentCustomRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

  private final JPAQueryFactory queryFactory;
  private final QComment comment = QComment.comment;

  @Override
  public List<Comment> getComments(CommentQueryCondition condition) {
    return switch (condition.orderBy()) {
      case CREATED_AT -> getCommentsByCreatedAt(condition);
      case LIKE_COUNT -> getCommentsByLikeCount(condition);
    };
  }

  // 등록순
  private List<Comment> getCommentsByCreatedAt(CommentQueryCondition condition) {
    return queryFactory.selectFrom(comment)
        .where(
            comment.article.id.eq(condition.articleId()),
            createdAtCursorCondition(condition))
        .orderBy(comment.createdAt.desc())
        .limit(condition.limit())
        .fetch();
  }

  // 좋아요순(2순위 등록순)
  private List<Comment> getCommentsByLikeCount(CommentQueryCondition condition) {
    return queryFactory.selectFrom(comment)
        .where(
            comment.article.id.eq(condition.articleId()),
            likeCountCursorCondition(condition))
        .orderBy(
            comment.likeCount.desc(),
            comment.createdAt.desc())
        .limit(condition.limit())
        .fetch();
  }

  private BooleanExpression createdAtCursorCondition(CommentQueryCondition condition) {
    if (condition.cursor() == null) {
      return null;
    }

    Instant cursor = Instant.parse(condition.cursor());

    // lt = less than(createdAt < cursor)
    return comment.createdAt.lt(cursor);
  }

  private BooleanExpression likeCountCursorCondition(CommentQueryCondition condition) {
    if (condition.cursor() == null) {
      return null;
    }

    long likeCursor = Long.parseLong(condition.cursor());

    // where likeCount < cursor or likeCount = cursor and createdAt < after
    return comment.likeCount.lt(likeCursor)
        .or(comment.likeCount.eq(likeCursor)
            .and(comment.createdAt.lt(condition.after())));
  }

  @Override
  public long countByArticleId(UUID articleId) {
    Long count = queryFactory.select(comment.count()).from(comment)
        .where(comment.article.id.eq(articleId))
        .fetchOne();

    return count != null ? count : 0L;
  }
}
