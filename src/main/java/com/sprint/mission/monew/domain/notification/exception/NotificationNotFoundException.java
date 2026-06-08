package com.sprint.mission.monew.domain.notification.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import java.util.Map;
import java.util.UUID;

public class NotificationNotFoundException extends NotificationException {

  private NotificationNotFoundException(Map<String, Object> details) {
    super(CommonErrorCode.NOTIFICATION_NOT_FOUND, details);
  }

  public static NotificationNotFoundException withId(UUID notificationId) {
    return new NotificationNotFoundException(Map.of("notificationId", notificationId));
  }
}