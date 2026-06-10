package com.sprint.mission.monew.domain.article.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ArticleRestoreFailedException extends ArticleException {

  private ArticleRestoreFailedException(Map<String, Object> details) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ArticleErrorCode.ARTICLE_RESTORE_FAILED, details);
  }

  public static ArticleRestoreFailedException withKey(String s3Key) {
    return new ArticleRestoreFailedException(Map.of("s3Key", s3Key));
  }
}
