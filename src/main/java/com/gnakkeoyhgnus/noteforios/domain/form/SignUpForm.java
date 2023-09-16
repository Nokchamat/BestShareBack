package com.gnakkeoyhgnus.noteforios.domain.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpForm {

  @NotEmpty
  @Email(message = "이메일을 확인해주세요.")
  private String email;

  @NotEmpty
  @Size(min = 5, message = "비밀번호는 최소 5자리 입니다.")
  private String password;

  @NotEmpty(message = "이름을 적어주세요.")
  private String name;

  @NotEmpty
  @Size(min = 1, max = 10, message = "닉네임은 최소 1자리, 최대 10자리 입니다.")
  private String nickname;

  @NotEmpty(message = "전화번호를 적어주세요.")
  @Size(max = 11, message = "전화번호를 확인해주세요.")
  @Pattern(regexp = "^[0-9]+$", message = "숫자만 적어주세요.")
  private String phoneNumber;

}
