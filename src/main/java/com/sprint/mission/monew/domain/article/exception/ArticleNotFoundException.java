package com.sprint.mission.monew.domain.article.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import java.util.Map;
import java.util.UUID;

public class ArticleNotFoundException extends ArticleException {

  private ArticleNotFoundException(Map<String, Object> details) {
    super(CommonErrorCode.ARTICLE_NOT_FOUND, details);
  }

  public static ArticleNotFoundException withId(UUID articleId) {
    return new ArticleNotFoundException(Map.of("articleId", articleId));
  }
}