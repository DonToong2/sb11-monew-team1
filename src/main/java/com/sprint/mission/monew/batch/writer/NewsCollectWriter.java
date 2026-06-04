package com.sprint.mission.monew.batch.writer;

import com.sprint.mission.monew.batch.ArticleUpsertService;
import com.sprint.mission.monew.batch.dto.NewsCollectItem;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsCollectWriter implements ItemWriter<NewsCollectItem> {

  private final ArticleUpsertService articleUpsertService;

  @Override
  public void write(@NonNull Chunk<? extends NewsCollectItem> chunk) {
  }

}
