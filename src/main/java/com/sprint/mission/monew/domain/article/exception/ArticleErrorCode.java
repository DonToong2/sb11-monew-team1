package com.sprint.mission.monew.domain.article.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ArticleErrorCode {

  ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "뉴스 기사를 찾을 수 없습니다."),
  ARTICLE_INVALID_CURSOR(HttpStatus.BAD_REQUEST, "커서 값의 형식이 올바르지 않습니다.");

  private final HttpStatus status;
  private final String message;
}
