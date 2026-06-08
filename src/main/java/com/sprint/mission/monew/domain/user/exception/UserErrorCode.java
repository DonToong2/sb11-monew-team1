package com.sprint.mission.monew.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  USER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
  USER_INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다."),
  USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
  USER_EMAIL_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "이메일 인증이 필요합니다."),
  USER_INVALID_EMAIL_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST,
      "유효하지 않거나 만료된 인증 토큰입니다."),
  USER_INVALID_UNLOCK_TOKEN(HttpStatus.BAD_REQUEST,
      "유효하지 않거나 만료된 잠금 해제 토큰입니다."),
  USER_INVALID_PASSWORD_RESET_CODE(HttpStatus.BAD_REQUEST,
      "유효하지 않거나 만료된 비밀번호 재설정 코드입니다."),
  USER_ACCOUNT_LOCKED(HttpStatus.LOCKED,
      "계정이 잠겼습니다. 이메일 인증을 통해 잠금을 해제해주세요.");

  private final HttpStatus status;
  private final String message;
}
