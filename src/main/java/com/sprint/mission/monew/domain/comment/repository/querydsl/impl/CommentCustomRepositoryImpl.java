package com.sprint.mission.monew.domain.comment.repository.querydsl.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.monew.common.dto.SortDirection;
import com.sprint.mission.monew.domain.comment.dto.CommentOrderBy;
import com.sprint.mission.monew.domain.comment.dto.CommentQueryCondition;
import com.sprint.mission.monew.domain.comment.dto.CommentResponse;
import com.sprint.mission.monew.domain.comment.entity.QComment;
import com.sprint.mission.monew.domain.comment.entity.QCommentLike;
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
  private final QCommentLike commentLike = QCommentLike.commentLike;

  @Override
  public List<CommentResponse> getComments(CommentQueryCondition condition, UUID userId) {

    return queryFactory.select(Projections.constructor(
              CommentResponse.class,
              comment.id,
              comment.article.id,
              comment.user.id,
              comment.user.nickname,
              comment.content,
              comment.likeCount,
              commentLike.id.isNotNull(),
              comment.createdAt
          ))
          .from(comment)
          .leftJoin(comment.user)
          .leftJoin(commentLike)
          .on(
              commentLike.user.id.eq(userId)
                  .and(commentLike.comment.id.eq(comment.id))
          )
          .where(comment.article.id.eq(condition.articleId()),
              comment.deletedAt.isNull(),
              condition.orderBy() == CommentOrderBy.CREATED_AT ?
                  createdAtCursorCondition(condition) : likeCountCursorCondition(condition))
          .orderBy(
              condition.orderBy() == CommentOrderBy.CREATED_AT ?
                  createdAtOrder(condition) : likeCountOrder(condition),
              createdAtOrder(condition)
          )
          .limit(condition.limit() + 1)
          .fetch();
  }

  // 오름차순/내림차순 정렬(등록순)
  private OrderSpecifier<?> createdAtOrder(CommentQueryCondition condition) {
    return condition.direction() == SortDirection.ASC ?
        comment.createdAt.asc() : comment.createdAt.desc();
  }

  // 오름차순/내림차순 정렬(좋아요순)
  private OrderSpecifier<?> likeCountOrder(CommentQueryCondition condition) {
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

    // where likeCount < cursor or likeCount = cursor and createdAt < after
    return condition.direction() == SortDirection.ASC ?
        comment.likeCount.gt(likeCursor) : comment.likeCount.lt(likeCursor);
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
