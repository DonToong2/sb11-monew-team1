package com.sprint.mission.monew.domain.article.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import com.sprint.mission.monew.common.exception.MonewException;
import java.util.Map;

public abstract class ArticleException extends MonewException {

  protected ArticleException(CommonErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}