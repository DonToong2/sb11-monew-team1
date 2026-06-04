package com.sprint.mission.monew.batch.reader;

import com.sprint.mission.monew.batch.dto.NewsCollectItem;
import com.sprint.mission.monew.domain.article.entity.ArticleSource;
import com.sprint.mission.monew.external.naver.NaverNewsClient;
import com.sprint.mission.monew.external.naver.dto.NaverNewsItem;
import com.sprint.mission.monew.external.rss.RssNewsParser;
import com.sprint.mission.monew.external.rss.dto.RssArticleDto;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    if (iterator == null) {
      iterator = loadItems().iterator();
    }

    return iterator.hasNext() ? iterator.next() : null;
  }

  private List<NewsCollectItem> loadItems() {
    List<NewsCollectItem> items = new ArrayList<>();

    loadNaver(items);
    loadRss(items, ArticleSource.HANKYUNG);
    loadRss(items, ArticleSource.CHOSUN);
    loadRss(items, ArticleSource.YONHAP);

    return items;
  }

  private void loadNaver(List<NewsCollectItem> items) {
    List<NaverNewsItem> fetched = naverNewsClient.fetchNews();

    for (NaverNewsItem item : fetched) {
      String sourceUrl = item.originallink() != null && !item.originallink().isBlank()
          ? item.originallink() : item.link();

      items.add(
          new NewsCollectItem(
              ArticleSource.NAVER,
              sourceUrl,
              NaverNewsClient.stripHtml(item.title()),
              NaverNewsClient.parseNaverDate(item.pubDate()),
              NaverNewsClient.stripHtml(item.description())
          )
      );
    }
  }

  private void loadRss(List<NewsCollectItem> items, ArticleSource source) {
    List<RssArticleDto> fetched = rssNewsParser.parse(source);

    for (RssArticleDto item : fetched) {
      items.add(
          new NewsCollectItem(
              source,
              item.sourceUrl(),
              item.title(),
              item.publishDate(),
              item.summary()
          )
      );
    }
  }
}
