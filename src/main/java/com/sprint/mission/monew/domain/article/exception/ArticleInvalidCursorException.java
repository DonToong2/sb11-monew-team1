package com.sprint.mission.monew.domain.article.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ArticleInvalidCursorException extends ArticleException {

  private ArticleInvalidCursorException(Map<String, Object> details) {
    super(HttpStatus.BAD_REQUEST, ArticleErrorCode.ARTICLE_INVALID_CURSOR, details);
  }

  public static ArticleInvalidCursorException withCursor(String cursor) {
    return new ArticleInvalidCursorException(Map.of("cursor", cursor));
  }
}