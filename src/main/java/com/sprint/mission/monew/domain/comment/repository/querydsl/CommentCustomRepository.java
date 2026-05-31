package com.sprint.mission.monew.domain.comment.repository.querydsl;

import com.sprint.mission.monew.domain.comment.dto.request.CommentQueryCondition;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import java.util.List;
import java.util.UUID;

public interface CommentCustomRepository {

  List<Comment> getComments(CommentQueryCondition condition);

  long countByArticleId(UUID articleId);

}
