package com.sprint.mission.monew.domain.comment.service;

import com.sprint.mission.monew.domain.comment.dto.response.CommentLikeResponse;
import com.sprint.mission.monew.domain.comment.entity.Comment;
import com.sprint.mission.monew.domain.comment.entity.CommentLike;
import com.sprint.mission.monew.domain.comment.exception.CommentLikeAlreadyExistsException;
import com.sprint.mission.monew.domain.comment.exception.CommentNotFoundException;
import com.sprint.mission.monew.domain.comment.mapper.CommentLikeMapper;
import com.sprint.mission.monew.domain.comment.repository.CommentLikeRepository;
import com.sprint.mission.monew.domain.comment.repository.CommentRepository;
import com.sprint.mission.monew.domain.user.entity.User;
import com.sprint.mission.monew.domain.user.exception.UserNotFoundException;
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

    // 사용자가 이미 댓글에 좋아요를 눌렀다면 예외처리
    if (commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)) {
      throw CommentLikeAlreadyExistsException.withId(userId, commentId);
    }

    User user = userRepository.findById(userId).orElseThrow(
        () -> UserNotFoundException.withId(userId)
    );
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> CommentNotFoundException.withId(commentId)
    );

    CommentLike commentLike = CommentLike.create(user, comment);

    comment.increaseLikeCount();
    CommentLike savedCommentLike = commentLikeRepository.save(commentLike);

    return commentLikeMapper.toResponse(savedCommentLike);
  }

}
