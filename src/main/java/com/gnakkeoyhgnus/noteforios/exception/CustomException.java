package com.gnakkeoyhgnus.noteforios.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;
  private final int status;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getDetail());
    this.errorCode = errorCode;
    this.status = errorCode.getHttpStatus().value();
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Getter
  public static class CustomExceptionResponse {

    private int status;
    private String code;
    private String message;

  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Getter
  public static class CustomExceptionValidResponse {

    private int status;
    private String code;
    private List<String> messages;
  }
}
