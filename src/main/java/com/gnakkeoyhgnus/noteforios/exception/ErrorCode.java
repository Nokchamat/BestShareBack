package com.gnakkeoyhgnus.noteforios.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  // 공용
  PERMISSION_DENIED_TO_DELETE(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다."),
  PERMISSION_DENIED_TO_UPDATE(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
  PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

  // 회원가입
  ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 이메일로 가입된 아이디가 존재합니다."),
  ALREADY_EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "이미 닉네임이 존재합니다."),

  // 로그인
  MISMATCH_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "이메일이나 비밀번호가 일치하지 않습니다."),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),

  // 토큰
  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다."),

  // 게시판
  NOT_FOUND_PAGE_SHARE_BOARD(HttpStatus.BAD_REQUEST, "페이지 공유 게시판을 찾을 수 없습니다."),

  //좋아요
  ALREADY_ADD_LIKES(HttpStatus.BAD_REQUEST, "이미 좋아요 한 게시물입니다."),
  NOT_FOUND_LIKES(HttpStatus.BAD_REQUEST, "좋아요를 찾을 수 없습니다."),

  //알림
  NOT_FOUND_NOTIFICATIONKEYWORDS(HttpStatus.BAD_REQUEST, "알림을 찾을 수 없습니다."),

  //팔로우
  ALREADY_ADD_FOLLOW(HttpStatus.BAD_REQUEST, "이미 팔로우 한 유저입니다."),
  NOT_FOUND_FOLLOW(HttpStatus.BAD_REQUEST, "팔로우를 찾을 수 없습니다."),

  ALREADY_EXIST_CHATTINGROOM(HttpStatus.BAD_REQUEST, "이미 상대방과의 채팅방이 존재합니다."),
  NOT_FOUND_CHATTINGROOM(HttpStatus.BAD_REQUEST, "채팅방을 찾을 수 없습니다."),

  //이메일 인증
  ALREADY_VERIFY_EMAIL(HttpStatus.BAD_REQUEST, "이미 인증된 이메일 입니다."),
  MISMATCH_VERIFY_CODE(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다."),
  PLEASE_VERIFY_EMAIL(HttpStatus.BAD_REQUEST, "이메일 인증을 필요합니다."),
  ;

  // 회원가입 Valid
  public static final String CHECK_THE_EMAIL = "이메일을 확인해주세요.";
  public static final String PASSWORD_TOO_SHORT_MIN5 = "비밀번호는 최소 5자리입니다.";
  public static final String WRITE_THE_NAME = "이름을 적어주세요.";
  public static final String CHECK_THE_NICKNAME_MIN1_MAX10 = "닉네임은 최소 1자리, 최대 10자리 입니다.";
  public static final String CHECK_THE_PHONE_NUMBER = "전화번호를 확인해주세요.";
  public static final String ONLY_NUMBER = "숫자만 적어주세요.";

  // 속지 공유 게시판 Valid
  public static final String WRITE_THE_TITLE = "제목을 적어주세요.";
  public static final String WRITE_THE_EXPLAIN = "설명을 적어주세요.";

  private final HttpStatus httpStatus;
  private final String detail;
}
