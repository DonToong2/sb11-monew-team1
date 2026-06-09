package com.sprint.mission.monew.batch.writer;

import com.sprint.mission.monew.batch.dto.CommentCleanupItem;
import com.sprint.mission.monew.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCleanupWriter implements ItemWriter<CommentCleanupItem> {

  private final CommentRepository commentRepository;

  @Override
  public void write(Chunk<? extends CommentCleanupItem> chunk) {
  }
}
