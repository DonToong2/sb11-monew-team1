package com.sprint.mission.monew.batch.log.backup.exception;

import com.sprint.mission.monew.batch.exception.BatchErrorCode;
import com.sprint.mission.monew.batch.exception.BatchException;

public class LogBackupFailedException extends BatchException {

  private LogBackupFailedException(String s3Key, Throwable cause) {
    super(BatchErrorCode.LOG_BACKUP_FAILED, s3Key, cause);
  }

  public static LogBackupFailedException withKey(String s3Key, Throwable cause) {
    return new LogBackupFailedException(s3Key, cause);
  }
}