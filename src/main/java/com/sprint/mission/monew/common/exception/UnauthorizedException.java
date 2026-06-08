package com.sprint.mission.monew.common.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends MonewException {

  private UnauthorizedException() {
    super(HttpStatus.UNAUTHORIZED, CommonErrorCode.UNAUTHORIZED, Map.of());
  }

  public static UnauthorizedException of() {
    return new UnauthorizedException();
  }
}
