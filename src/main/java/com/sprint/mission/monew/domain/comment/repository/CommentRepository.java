package com.sprint.mission.monew.domain.comment.repository;

import com.sprint.mission.monew.domain.comment.entity.Comment;
import com.sprint.mission.monew.domain.comment.repository.querydsl.CommentCustomRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, UUID>, CommentCustomRepository {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("""
      update Comment c set c.likeCount = c.likeCount + 1
            where c.id = :commentId
      """)
  void increaseLikeCount(UUID commentId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("""
      update Comment c set c.likeCount = c.likeCount - 1
            where c.id = :commentId and c.likeCount > 0
      """)
  void decreaseLikeCount(UUID commentId);

  List<Comment> findByArticleIdOrderByCreatedAtDesc(UUID articleId, Pageable pageable);

  List<Comment> findByArticleIdOrderByLikeCountDescCreatedAtDesc(UUID articleId, Pageable pageable);
}
