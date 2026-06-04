package com.sprint.mission.monew.batch.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mission.monew.batch.dto.NewsCollectItem;
import com.sprint.mission.monew.domain.article.entity.ArticleSource;
import com.sprint.mission.monew.external.naver.NaverNewsClient;
import com.sprint.mission.monew.external.naver.dto.NaverNewsItem;
import com.sprint.mission.monew.external.rss.RssNewsParser;
import com.sprint.mission.monew.external.rss.dto.RssArticleDto;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NewsCollectReaderTest {

  @Mock
  private NaverNewsClient naverNewsClient;

  @Mock
  private RssNewsParser rssNewsParser;

  private NewsCollectReader reader;

  @BeforeEach
  void setup() {
    reader = new NewsCollectReader(
        naverNewsClient,
        rssNewsParser
    );
  }

  @Nested
  @DisplayName("뉴스 기사 읽기")
  class Reader {

    @Test
    @DisplayName("네이버 기사 조회")
    void 기사_조회_네이버() {
      // given
      NaverNewsItem item = new NaverNewsItem(
          "제목", "https://example.com/1", "https://example.com/1",
          "요약", "Mon, 29 May 2026 00:00:00 +0900");

      when(naverNewsClient.fetchNews()).thenReturn(List.of(item));
      when(rssNewsParser.parse(any())).thenReturn(List.of());

      // when
      NewsCollectItem result = reader.read();

      // then
      assertThat(result).isNotNull();
      assertThat(result.source()).isEqualTo(ArticleSource.NAVER);
    }

    @Test
    @DisplayName("RSS 기사 조회")
    void 기사_조회_RSS() {
      // given
      // HANKYUNG 기사만 있음
      RssArticleDto item = new RssArticleDto(
          ArticleSource.HANKYUNG, "https://hankyung.com/1", "한경 기사", Instant.now(), "요약");

      when(naverNewsClient.fetchNews()).thenReturn(List.of());
      when(rssNewsParser.parse(ArticleSource.HANKYUNG)).thenReturn(List.of(item));
      when(rssNewsParser.parse(ArticleSource.CHOSUN)).thenReturn(List.of());
      when(rssNewsParser.parse(ArticleSource.YONHAP)).thenReturn(List.of());

      // when
      NewsCollectItem result = reader.read();

      // then
      assertThat(result).isNotNull();
      assertThat(result.source()).isEqualTo(ArticleSource.HANKYUNG);
    }

    @Test
    @DisplayName("모든 기사를 읽을 시 null 반환")
    void 모든_기사_조회_후_null() {
      // when
      when(naverNewsClient.fetchNews()).thenReturn(List.of());
      when(rssNewsParser.parse(any())).thenReturn(List.of());

      // then
      assertThat(reader.read()).isNull();
    }
  }
}
