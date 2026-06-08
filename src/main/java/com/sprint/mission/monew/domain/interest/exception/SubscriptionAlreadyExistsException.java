package com.sprint.mission.monew.domain.interest.exception;

import com.sprint.mission.monew.common.exception.CommonErrorCode;
import java.util.Map;
import java.util.UUID;

public class SubscriptionAlreadyExistsException extends InterestException {

  private SubscriptionAlreadyExistsException(Map<String, Object> details) {
    super(CommonErrorCode.SUBSCRIPTION_ALREADY_EXISTS, details);
  }

  public static SubscriptionAlreadyExistsException withIds(UUID interestId, UUID userId) {
    return new SubscriptionAlreadyExistsException(
        Map.of("interestId", interestId, "userId", userId));
  }
}