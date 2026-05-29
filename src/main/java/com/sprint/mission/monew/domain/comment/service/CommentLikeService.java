package com.sprint.mission.monew.domain.comment.service;

import com.sprint.mission.monew.domain.comment.dto.response.CommentLikeResponse;
import com.sprint.mission.monew.domain.comment.mapper.CommentLikeMapper;
import com.sprint.mission.monew.domain.comment.repository.CommentLikeRepository;
import com.sprint.mission.monew.domain.comment.repository.CommentRepository;
import com.sprint.mission.monew.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {

  private final CommentLikeRepository commentLikeRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;
  private final CommentLikeMapper commentLikeMapper;

  @Transactional
  public CommentLikeResponse create(UUID commentId, UUID userId) {
    return null;
  }

}
