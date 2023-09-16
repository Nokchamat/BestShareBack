package com.gnakkeoyhgnus.noteforios.exception;

import com.gnakkeoyhgnus.noteforios.exception.CustomException.CustomExceptionResponse;
import com.gnakkeoyhgnus.noteforios.exception.CustomException.CustomExceptionValidResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomExceptionValidResponse> handleValidationException(
      MethodArgumentNotValidException exception) {

    List<String> errors = exception.getBindingResult().getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    CustomExceptionValidResponse errorResponse = new CustomExceptionValidResponse(
        HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  // CustomException 이 발생했을 때 처리하는 핸들러 메서드
  @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
  public ResponseEntity<CustomExceptionResponse> exceptionHandler(
      CustomException exception) {
    log.error("'{}':'{}'", exception.getErrorCode(), exception.getErrorCode().getDetail());
    return ResponseEntity
        .status(exception.getStatus())
        .body(CustomExceptionResponse.builder()
            .message(exception.getMessage())
            .code(exception.getErrorCode().getDetail())
            .status(exception.getStatus()).build());
  }

}
