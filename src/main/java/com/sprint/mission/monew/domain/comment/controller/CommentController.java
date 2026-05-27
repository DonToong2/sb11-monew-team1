package com.sprint.mission.monew.domain.comment.controller;

import com.sprint.mission.monew.domain.comment.controller.api.CommentApi;
import com.sprint.mission.monew.domain.comment.dto.request.CommentCreateRequest;
import com.sprint.mission.monew.domain.comment.dto.response.CommentResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentController implements CommentApi {

    @Override
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody @Valid CommentCreateRequest request) {
        return ResponseEntity.status(null).body(null);
    }
}
