package com.sprint.mission.monew.batch.writer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.monew.batch.ArticleUpsertService;
import com.sprint.mission.monew.batch.dto.NewsCollectItem;
import com.sprint.mission.monew.domain.article.entity.ArticleSource;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

@ExtendWith(MockitoExtension.class)
public class NewsCollectWriterTest {

  @Mock
  private ArticleUpsertService articleUpsertService;

  private NewsCollectWriter newsCollectWriter;

  @BeforeEach
  void setUp() {
    newsCollectWriter = new NewsCollectWriter(articleUpsertService);
  }

  @Nested
  @DisplayName("기사 저장하기")
  class Writer {

    @Test
    @DisplayName("기사 단건 저장")
    void 기사_단건_저장() throws Exception {
      // given
      Instant publishDate = Instant.now();
      NewsCollectItem item = new NewsCollectItem(
          ArticleSource.HANKYUNG, "https://hankyung.com/1", "한경 기사", publishDate, "요약");

      Chunk<NewsCollectItem> chunk = new Chunk<>(List.of(item));

      // when
      newsCollectWriter.write(chunk);

      // then
      verify(articleUpsertService).upsert(
          ArticleSource.HANKYUNG, "https://hankyung.com/1", "한경 기사", publishDate, "요약");
    }

    @Test
    @DisplayName("기사 다건 저장")
    void 기사_다건_저장() {
      // given
      // first는 NAVER 기사, second는 HANKYUNG 기사, third는 CHOSUN 기사, fourth는 HANKYUNG 기사
      NewsCollectItem first =
          new NewsCollectItem(
              ArticleSource.NAVER,
              "https://naver.com/1", "네이버 기사 제목1", Instant.now(), "요약1");
      NewsCollectItem second =
          new NewsCollectItem(
              ArticleSource.HANKYUNG,
              "https://hankyung.com/1", "한경 기사 제목1", Instant.now(), "요약1");
      NewsCollectItem third =
          new NewsCollectItem(
              ArticleSource.CHOSUN,
              "https://chosun.com/1", "조선 기사 제목1", Instant.now(), "요약1");
      NewsCollectItem fourth =
          new NewsCollectItem(ArticleSource.HANKYUNG,
              "https://hankyung.com/2", "한경 기사 제목2", Instant.now(), "요약2");

      Chunk<NewsCollectItem> chunk = new Chunk<>(List.of(first, second, third, fourth));

      // when
      newsCollectWriter.write(chunk);

      // then
      // ArticleUpsertService가 4번 호출되어야함
      verify(articleUpsertService, times(4))
          .upsert(any(), any(), any(), any(), any());
    }
  }
}
