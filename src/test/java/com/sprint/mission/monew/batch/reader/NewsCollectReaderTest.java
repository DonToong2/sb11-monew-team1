package com.sprint.mission.monew.batch.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
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
    @DisplayName("네이버 기사 조회 성공")
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
    @DisplayName("RSS 기사 조회 성공")
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
    @DisplayName("Naver 출처 수집 실패 시 다른 출처는 계속 수집한다")
    void _Naver_출처_실패_시_다른_출처는_계속_수집한다() {
      // given
      RssArticleDto rssItem = new RssArticleDto(
          ArticleSource.HANKYUNG, "https://hankyung.com/1", "한경 기사", Instant.now(), "요약");
      given(naverNewsClient.fetchNews()).willThrow(new RuntimeException("Naver API 오류"));
      given(rssNewsParser.parse(eq(ArticleSource.HANKYUNG))).willReturn(List.of(rssItem));
      given(rssNewsParser.parse(eq(ArticleSource.CHOSUN))).willReturn(List.of());
      given(rssNewsParser.parse(eq(ArticleSource.YONHAP))).willReturn(List.of());

      // when & then — 예외 없이 완료, HANKYUNG은 upsert 호출됨
      // when
      NewsCollectItem result = reader.read();

      // then
      assertThat(result).isNotNull();
      assertThat(result.source()).isEqualTo(ArticleSource.HANKYUNG);
    }

    @Test
    @DisplayName("RSS 출처 수집 실패 시 다른 출처는 계속 읽는다.")
    void _RSS_출처_실패_시_다른_출처는_계속_수집한다() {
      // given
      RssArticleDto chosunItem = new RssArticleDto(
          ArticleSource.CHOSUN, "https://chosun.com/1", "조선 기사", Instant.now(), "요약");
      given(naverNewsClient.fetchNews()).willReturn(List.of());
      given(rssNewsParser.parse(eq(ArticleSource.HANKYUNG))).willThrow(new RuntimeException("RSS 오류"));
      given(rssNewsParser.parse(eq(ArticleSource.CHOSUN))).willReturn(List.of(chosunItem));
      given(rssNewsParser.parse(eq(ArticleSource.YONHAP))).willReturn(List.of());

      // when
      NewsCollectItem result = reader.read();

      // then
      assertThat(result).isNotNull();
      assertThat(result.source()).isEqualTo(ArticleSource.CHOSUN);
    }

    @Test
    @DisplayName("originallink·link 모두 null인 Naver 기사는 null sourceUrl로 upsert를 호출한다")
    void originallink_link_모두_null인_기사는_null_sourceUrl로_upsert를_호출한다() {
      // given — originallink, link 모두 null → sourceUrl = null (skip은 ArticleUpsertService 내부 처리)
      NaverNewsItem item = new NaverNewsItem("제목", null, null, "요약",
          "Mon, 29 May 2026 00:00:00 +0900");
      given(naverNewsClient.fetchNews()).willReturn(List.of(item));
      given(rssNewsParser.parse(any())).willReturn(List.of());

      // when
      NewsCollectItem result = reader.read();

      // then
      assertThat(result).isNotNull();
      assertThat(result.sourceUrl()).isNull();
    }
  }
}
