package com.gnakkeoyhgnus.noteforios.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  ;

  private final HttpStatus httpStatus;
  private final String detail;
}
