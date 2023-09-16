package com.gnakkeoyhgnus.noteforios.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  // 회원가입
  ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 이메일로 가입된 아이디가 존재합니다."),
  ALREADY_EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "이미 닉네임이 존재합니다."),

  //로그인
  MISMATCH_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "이메일이나 비밀번호가 일치하지 않습니다."),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),

  //토큰
  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다.")

  ;

  private final HttpStatus httpStatus;
  private final String detail;
}
