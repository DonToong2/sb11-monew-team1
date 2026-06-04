package com.sprint.mission.monew.batch.reader;

import com.sprint.mission.monew.batch.dto.NewsCollectItem;
import com.sprint.mission.monew.external.naver.NaverNewsClient;
import com.sprint.mission.monew.external.rss.RssNewsParser;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class NewsCollectReader implements ItemReader<NewsCollectItem> {

  private final NaverNewsClient naverNewsClient;
  private final RssNewsParser rssNewsParser;

  private Iterator<NewsCollectItem> iterator;

  @Override
  public NewsCollectItem read() {
    return null;
  }
}
