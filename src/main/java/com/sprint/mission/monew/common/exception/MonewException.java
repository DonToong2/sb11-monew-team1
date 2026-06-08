package com.sprint.mission.monew.common.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public abstract class MonewException extends RuntimeException {

  private final CommonErrorCode errorCode;
  private final Map<String, Object> details;

  protected MonewException(CommonErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.details = details;
  }
}