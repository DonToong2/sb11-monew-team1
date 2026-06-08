package com.sprint.mission.monew.domain.user.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import java.util.Map;

public class UserInvalidUnlockTokenException extends UserException {

  private UserInvalidUnlockTokenException(CommonErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static UserInvalidUnlockTokenException withToken(String token) {
    return new UserInvalidUnlockTokenException(
        CommonErrorCode.USER_INVALID_UNLOCK_TOKEN,
        Map.of("token", "REDACTED"));
  }
}