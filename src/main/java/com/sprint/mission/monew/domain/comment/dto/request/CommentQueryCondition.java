package com.sprint.mission.monew.domain.comment.dto.request;

import com.sprint.mission.monew.common.dto.SortDirection;
import java.time.Instant;
import java.util.UUID;

public record CommentQueryCondition(
    UUID articleId,
    CommentOrderBy orderBy,
    SortDirection direction,
    String cursor,
    Instant after,
    int limit
) {

}
