package com.sprint.mission.monew.domain.comment.repository.querydsl.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.monew.common.dto.SortDirection;
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
        .leftJoin(comment.user).fetchJoin()
        .leftJoin(comment.article).fetchJoin()
        .where(
            comment.article.id.eq(condition.articleId()),
            comment.deletedAt.isNull(), // 논리 삭제는 조회 안되도록
            createdAtCursorCondition(condition))
        .orderBy(createdAtOrder(condition))
        .limit(condition.limit() + 1)
        .fetch();
  }

  // 좋아요순(2순위 등록순)
  private List<Comment> getCommentsByLikeCount(CommentQueryCondition condition) {
    return queryFactory.selectFrom(comment)
        .leftJoin(comment.user).fetchJoin()
        .leftJoin(comment.article).fetchJoin()
        .where(
            comment.article.id.eq(condition.articleId()),
            comment.deletedAt.isNull(), // 논리 삭제는 조회 안되도록
            likeCountCursorCondition(condition))
        .orderBy(
            likeCountOrder(condition),
            createdAtOrder(condition))
        .limit(condition.limit() + 1)
        .fetch();
  }

  // 오름차순/내림차순 정렬(등록순)
  private OrderSpecifier createdAtOrder(CommentQueryCondition condition) {
    return condition.direction() == SortDirection.ASC ?
        comment.createdAt.asc() : comment.createdAt.desc();
  }

  // 오름차순/내림차순 정렬(좋아요순)
  private OrderSpecifier likeCountOrder(CommentQueryCondition condition) {
    return condition.direction() == SortDirection.ASC ?
        comment.likeCount.asc() : comment.likeCount.desc();
  }

  // 등록순 커서
  private BooleanExpression createdAtCursorCondition(CommentQueryCondition condition) {
    if (condition.cursor() == null) {
      return null;
    }

    Instant cursor = Instant.parse(condition.cursor());

    // lt = less than(createdAt < cursor)
    return condition.direction() == SortDirection.ASC ?
        comment.createdAt.gt(cursor) : comment.createdAt.lt(cursor);
  }

  // 좋아요순 커서
  private BooleanExpression likeCountCursorCondition(CommentQueryCondition condition) {
    if (condition.cursor() == null) {
      return null;
    }

    long likeCursor = Long.parseLong(condition.cursor());

    // after null시 처리
    if (condition.after() == null) {
      return condition.direction() == SortDirection.ASC ?
          comment.likeCount.gt(likeCursor) : comment.likeCount.lt(likeCursor);
    }

    // where likeCount < cursor or likeCount = cursor and createdAt < after
    return condition.direction() == SortDirection.ASC ?
        comment.likeCount.gt(likeCursor)
            .or(comment.likeCount.eq(likeCursor)
                .and(comment.createdAt.gt(condition.after())))
        : comment.likeCount.lt(likeCursor)
            .or(comment.likeCount.eq(likeCursor)
                .and(comment.createdAt.lt(condition.after())));
  }

  @Override
  public long countByArticleId(UUID articleId) {
    Long count = queryFactory.select(comment.count()).from(comment)
        .where(comment.article.id.eq(articleId),
            comment.deletedAt.isNull()) // 논리 삭제는 조회 안되도록
        .fetchOne();

    return count != null ? count : 0L;
  }
}
