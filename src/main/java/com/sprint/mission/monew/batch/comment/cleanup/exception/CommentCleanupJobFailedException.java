package com.sprint.mission.monew.batch.comment.cleanup.exception;

import com.sprint.mission.monew.batch.exception.BatchErrorCode;
import com.sprint.mission.monew.batch.exception.BatchException;

public class CommentCleanupJobFailedException extends BatchException {

  private CommentCleanupJobFailedException(Throwable cause) {
    super(BatchErrorCode.COMMENT_CLEANUP_JOB_FAILED, null, cause);
  }

  public static CommentCleanupJobFailedException wrap(Throwable cause) {
    return new CommentCleanupJobFailedException(cause);
  }
}
