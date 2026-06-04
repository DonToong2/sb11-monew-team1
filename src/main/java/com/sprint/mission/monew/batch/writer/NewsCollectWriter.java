package com.sprint.mission.monew.batch.writer;

import com.sprint.mission.monew.batch.ArticleUpsertService;
import com.sprint.mission.monew.batch.NewsCollectMetrics;
import com.sprint.mission.monew.batch.dto.NewsCollectItem;
import com.sprint.mission.monew.domain.article.entity.ArticleSource;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCollectWriter implements ItemWriter<NewsCollectItem> {

  private final ArticleUpsertService articleUpsertService;
  private final NewsCollectMetrics newsCollectMetrics;

  @Override
  public void write(@NonNull Chunk<? extends NewsCollectItem> chunk) {

    Map<ArticleSource, Integer> counter = new EnumMap<>(ArticleSource.class);

    for (NewsCollectItem item : chunk.getItems()) {

      counter.merge(item.source(), 1, Integer::sum);

      try {
        articleUpsertService.upsert(
            item.source(),
            item.sourceUrl(),
            item.title(),
            item.publishDate(),
            item.summary()
        );

      } catch (Exception e) {
        newsCollectMetrics.countFailed(item.source());
        log.warn("{} 기사 단건 처리 실패: link={}", item.source(), item.sourceUrl(), e);
      }
    }

    counter.forEach(newsCollectMetrics::countCollected);
  }

}
