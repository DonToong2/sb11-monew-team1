package com.sprint.mission.monew.domain.interest.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import com.sprint.mission.monew.common.exception.MonewException;
import java.util.Map;

public abstract class InterestException extends MonewException {

  protected InterestException(CommonErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}