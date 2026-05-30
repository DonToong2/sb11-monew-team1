package com.sprint.mission.monew.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CommentOrderBy {
  @JsonProperty("createdAt") CREATED_AT,
  @JsonProperty("likeCount") LIKE_COUNT
}
