package com.sprint.mission.monew.domain.comment.repository.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.monew.domain.comment.dto.request.CommentQueryCondition;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import com.sprint.mission.monew.domain.comment.repository.querydsl.CommentCustomRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Comment> getComments(CommentQueryCondition condition) {
    return List.of();
  }

  @Override
  public long countByArticleId(UUID articleId) {
    return 0;
  }

}
