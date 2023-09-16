package com.gnakkeoyhgnus.noteforios.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  // 회원가입
  ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 이메일로 가입된 아이디가 존재합니다."),
  ALREADY_EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "이미 닉네임이 존재합니다.")
  ;

  private final HttpStatus httpStatus;
  private final String detail;
}
