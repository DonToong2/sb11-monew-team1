package com.sprint.mission.monew.batch.reader;

import com.sprint.mission.monew.batch.NewsCollectMetrics;
import com.sprint.mission.monew.batch.dto.NewsCollectItem;
import com.sprint.mission.monew.domain.article.entity.ArticleSource;
import com.sprint.mission.monew.external.naver.NaverNewsClient;
import com.sprint.mission.monew.external.naver.dto.NaverNewsItem;
import com.sprint.mission.monew.external.rss.RssNewsParser;
import com.sprint.mission.monew.external.rss.dto.RssArticleDto;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCollectReader implements ItemReader<NewsCollectItem> {

  private final NaverNewsClient naverNewsClient;
  private final RssNewsParser rssNewsParser;
  private final NewsCollectMetrics newsCollectMetrics;

  private Iterator<NewsCollectItem> iterator;

  @Override
  public NewsCollectItem read() {
    if (iterator == null) {
      long start = System.nanoTime();

      List<NewsCollectItem> items = loadItems();
      newsCollectMetrics.recordCollectDuration(Duration.ofNanos(System.nanoTime() - start));

      iterator = items.iterator();
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

    try {
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

      newsCollectMetrics.countCollected(ArticleSource.NAVER, fetched.size());
      log.info("Naver 뉴스 수집 완료: {}건", fetched.size());

    } catch (Exception e) {
      log.error("Naver 뉴스 수집 실패", e);
      newsCollectMetrics.countFailed(ArticleSource.NAVER);
    }
  }

  private void loadRss(List<NewsCollectItem> items, ArticleSource source) {

    try {
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

      newsCollectMetrics.countCollected(source, fetched.size());
      log.info("{} RSS 수집 완료: {}건", source, fetched.size());

    } catch (Exception e) {
      log.error("{} RSS 수집 실패", source, e);
      newsCollectMetrics.countFailed(source);
    }
  }
}
