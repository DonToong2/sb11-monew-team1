package com.sprint.mission.monew.batch.notification.cleanup.exception;

import com.sprint.mission.monew.batch.exception.BatchErrorCode;
import com.sprint.mission.monew.batch.exception.BatchException;

public class NotificationCleanupJobFailedException extends BatchException {

  private NotificationCleanupJobFailedException(Throwable cause) {
    super(BatchErrorCode.NOTIFICATION_CLEANUP_JOB_FAILED, null, cause);
  }

  public static NotificationCleanupJobFailedException wrap(Throwable cause) {
    return new NotificationCleanupJobFailedException(cause);
  }
}
