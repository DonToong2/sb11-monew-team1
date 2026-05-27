package com.sprint.mission.monew.domain.comment.controller.api;

import com.sprint.mission.monew.domain.comment.dto.request.CommentCreateRequest;
import com.sprint.mission.monew.domain.comment.dto.response.CommentResponse;
import org.springframework.http.ResponseEntity;

public interface CommentApi {
    ResponseEntity<CommentResponse> createComment(CommentCreateRequest request);
}
