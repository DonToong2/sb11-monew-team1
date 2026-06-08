package com.sprint.mission.monew.domain.interest.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InterestErrorCode {

  INTEREST_NOT_FOUND(HttpStatus.NOT_FOUND, "관심사를 찾을 수 없습니다."),
  INTEREST_ALREADY_EXISTS(HttpStatus.CONFLICT, "유사한 관심사가 이미 존재합니다.");

  private final HttpStatus status;
  private final String message;
}
