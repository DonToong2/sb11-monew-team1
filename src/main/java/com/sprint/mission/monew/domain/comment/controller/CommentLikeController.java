package com.sprint.mission.monew.domain.comment.controller;

import com.sprint.mission.monew.domain.comment.controller.api.CommentLikeApi;
import com.sprint.mission.monew.domain.comment.dto.response.CommentLikeResponse;
import com.sprint.mission.monew.domain.comment.service.CommentLikeService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentLikeController implements CommentLikeApi {

  private final CommentLikeService commentLikeService;

  @Override
  @PostMapping("/{commentId}/comment-likes")
  public ResponseEntity<CommentLikeResponse> createCommentLike(
      @PathVariable UUID commentId,
      @RequestHeader("Monew-Request-User-ID") UUID userId) {
    return null;
  }

}
