package com.sprint.mission.monew.domain.comment.controller.api;

import com.sprint.mission.monew.domain.comment.dto.response.CommentLikeResponse;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

public interface CommentLikeApi {

  ResponseEntity<CommentLikeResponse> createCommentLike(
      @PathVariable UUID commentId,
      @RequestHeader("Monew-Request-User-ID") UUID userId
  );

}
