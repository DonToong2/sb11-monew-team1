package com.sprint.mission.monew.batch.reader;

import com.sprint.mission.monew.batch.dto.CommentCleanupItem;
import com.sprint.mission.monew.domain.comment.repository.CommentRepository;
import java.time.Instant;
import java.util.Iterator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class CommentCleanupReader implements ItemReader<CommentCleanupItem> {

  private final CommentRepository commentRepository;

  @Value("${batch.comment-cleanup.chunk-size}")
  private int chunkSize;

  private Instant threshold;
  private Instant lastDeletedAt;
  private UUID lastId;

  private Iterator<CommentCleanupItem> iterator;

  @Override
  public CommentCleanupItem read() {
    return null;
  }

}
