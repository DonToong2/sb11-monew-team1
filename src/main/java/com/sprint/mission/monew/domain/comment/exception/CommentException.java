package com.sprint.mission.monew.domain.comment.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import com.sprint.mission.monew.common.exception.MonewException;
import java.util.Map;

public abstract class CommentException extends MonewException {

  protected CommentException(CommonErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}