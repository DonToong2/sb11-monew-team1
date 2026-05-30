package com.sprint.mission.monew.domain.comment.repository.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.monew.domain.comment.dto.request.CommentQueryCondition;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import com.sprint.mission.monew.domain.comment.entity.QComment;
import com.sprint.mission.monew.domain.comment.repository.querydsl.CommentCustomRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository {

  private final JPAQueryFactory queryFactory;
  private final QComment comment = QComment.comment;

  @Override
  public List<Comment> getComments(CommentQueryCondition condition) {
    return queryFactory.selectFrom(comment)
        .where(comment.article.id.eq(condition.articleId()))
        .orderBy(comment.createdAt.desc())
        .limit(condition.limit())
        .fetch();
  }

  @Override
  public long countByArticleId(UUID articleId) {
    Long count = queryFactory.select(comment.count()).from(comment)
        .where(comment.article.id.eq(articleId))
        .fetchOne();

    return count != null ? count : 0L;
  }

}
