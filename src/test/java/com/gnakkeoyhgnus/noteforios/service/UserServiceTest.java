package com.gnakkeoyhgnus.noteforios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.SignInForm;
import com.gnakkeoyhgnus.noteforios.domain.form.SignUpForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import com.gnakkeoyhgnus.noteforios.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@DisplayName("유저 서비스 테스트")
class UserServiceTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @MockBean
  private AmazonS3Service amazonS3Service;
  @Autowired
  private UserService userService;

  @BeforeEach
  void init() {
    userRepository.deleteAll();
  }


  @DisplayName("회원가입 - 이미지 업로드")
  @Test
  void signUp_ImageUpload() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("test@test.com")
        .password("12345678")
        .name("테스트")
        .nickname("닉네임")
        .phoneNumber("01012341234")
        .build();

    MultipartFile multipartFile = mock(MultipartFile.class);

    when(amazonS3Service.uploadImage(any(MultipartFile.class), any(String.class)))
        .thenReturn("uploadUrl");

    //when
    userService.signUp(signUpForm, multipartFile);
    User user = userRepository.findByEmail(signUpForm.getEmail()).orElseThrow(
        () -> new CustomException(ErrorCode.NOT_FOUND_USER));

    //then
    assertEquals(signUpForm.getEmail(), user.getEmail());
    assertEquals(signUpForm.getName(), user.getName());
    assertEquals(signUpForm.getNickname(), user.getNickname());
    assertEquals(signUpForm.getPhoneNumber(), user.getPhoneNumber());
    assertEquals("uploadUrl", user.getProfileImageUrl());
  }

  @DisplayName("회원가입 - 이미지 업로드 안 했을 때")
  @Test
  void signUp_ImageEmpty() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("test@test.com")
        .password("12345678")
        .name("테스트")
        .nickname("닉네임")
        .phoneNumber("01012341234")
        .build();

    MultipartFile multipartFile = mock(MultipartFile.class);

    when(multipartFile.isEmpty())
        .thenReturn(true);

    //when
    userService.signUp(signUpForm, multipartFile);
    User user = userRepository.findByEmail(signUpForm.getEmail()).orElseThrow(
        () -> new CustomException(ErrorCode.NOT_FOUND_USER));

    //then
    assertEquals(signUpForm.getEmail(), user.getEmail());
    assertEquals(signUpForm.getName(), user.getName());
    assertEquals(signUpForm.getNickname(), user.getNickname());
    assertEquals(signUpForm.getPhoneNumber(), user.getPhoneNumber());
    assertEquals("testDefaultImageUrl", user.getProfileImageUrl());
  }

  @DisplayName("로그인 성공")
  @Test
  void signIn_Success() {
    //given
    SignInForm sign = SignInForm.builder()
        .email("test@test.com")
        .password("12345678")
        .build();

    userRepository.save(User.builder()
        .email(sign.getEmail())
        .password(passwordEncoder.encode(sign.getPassword()))
        .build());

    //when
    String token = userService.signIn(sign);

    //then
    assertTrue(jwtTokenProvider.isTokenValid(token));
  }

  @DisplayName("로그인 실패 - 비밀번호 틀림")
  @Test
  void signIn_Fail() {
    //given
    SignInForm sign = SignInForm.builder()
        .email("test@test.com")
        .password("12345678")
        .build();

    userRepository.save(User.builder()
        .email(sign.getEmail())
        .password(passwordEncoder.encode(sign.getPassword() + 1))
        .build());

    //when
    CustomException exception = assertThrows(CustomException.class, () -> userService.signIn(sign));

    //then
    assertEquals(ErrorCode.MISMATCH_EMAIL_OR_PASSWORD, exception.getErrorCode());
  }

}