package com.sprint.mission.monew.domain.notification.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import com.sprint.mission.monew.common.exception.MonewException;
import java.util.Map;

public abstract class NotificationException extends MonewException {

  protected NotificationException(CommonErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }
}