package com.sprint.mission.monew.domain.comment.dto.request;

import com.sprint.mission.monew.common.dto.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record CommentQueryCondition(
    UUID articleId,
    @NotNull CommentOrderBy orderBy,
    @NotNull SortDirection direction,
    String cursor,
    Instant after,
    @NotNull @Min(1) int limit
) {

}
