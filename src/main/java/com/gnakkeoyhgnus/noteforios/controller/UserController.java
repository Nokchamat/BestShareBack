package com.gnakkeoyhgnus.noteforios.controller;

import com.gnakkeoyhgnus.noteforios.domain.form.SignInForm;
import com.gnakkeoyhgnus.noteforios.domain.form.SignUpForm;
import com.gnakkeoyhgnus.noteforios.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/sign-up")
  public ResponseEntity<String> signUp(@RequestPart @Valid SignUpForm signUpForm,
      @RequestPart MultipartFile profileImage) {
    userService.signUp(signUpForm, profileImage);

    return ResponseEntity.ok("회원가입이 완료되었습니다.");
  }

  @PostMapping("/sign-in")
  public ResponseEntity<Void> signIn(@RequestBody SignInForm signInForm) {

    String token = userService.signIn(signInForm);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, token);

    return ResponseEntity.ok().headers(headers).body(null);
  }

}
