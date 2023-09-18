package com.gnakkeoyhgnus.noteforios.domain.form;

import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.CHECK_THE_EMAIL;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.CHECK_THE_NICKNAME_MIN1_MAX10;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.CHECK_THE_PHONE_NUMBER;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.ONLY_NUMBER;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.PASSWORD_TOO_SHORT_MIN5;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.WRITE_THE_NAME;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpForm {

  @Email(message = CHECK_THE_EMAIL)
  private String email;

  @Size(min = 5, message = PASSWORD_TOO_SHORT_MIN5)
  private String password;

  @NotEmpty(message = WRITE_THE_NAME)
  private String name;

  @Size(min = 1, max = 10, message = CHECK_THE_NICKNAME_MIN1_MAX10)
  private String nickname;

  @Size(min = 11, max = 11, message = CHECK_THE_PHONE_NUMBER)
  @Pattern(regexp = "^[0-9]+$", message = ONLY_NUMBER)
  private String phoneNumber;

}
