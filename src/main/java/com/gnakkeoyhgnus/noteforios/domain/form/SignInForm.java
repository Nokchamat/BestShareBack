package com.gnakkeoyhgnus.noteforios.domain.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInForm {

  private String email;
  private String password;

}
