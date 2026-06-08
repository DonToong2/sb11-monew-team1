package com.sprint.mission.monew.domain.article.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import java.util.Map;

public class ArticleInvalidCursorException extends ArticleException {

  private ArticleInvalidCursorException(Map<String, Object> details) {
    super(CommonErrorCode.ARTICLE_INVALID_CURSOR, details);
  }

  public static ArticleInvalidCursorException withCursor(String cursor) {
    return new ArticleInvalidCursorException(Map.of("cursor", cursor));
  }
}
