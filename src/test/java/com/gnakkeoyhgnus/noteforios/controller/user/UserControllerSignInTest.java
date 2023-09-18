package com.gnakkeoyhgnus.noteforios.controller.user;

import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.MISMATCH_EMAIL_OR_PASSWORD;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.NOT_FOUND_USER;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.SignInForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.service.AmazonS3Service;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("유저 컨트롤러 - 로그인 테스트")
class UserControllerSignInTest {

  @MockBean
  private AmazonS3Service amazonS3Service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private static final String url = "http://localhost:8080";

  @BeforeEach
  void init() {
    userRepository.deleteAll();
  }

  @DisplayName("로그인 성공")
  @Test
  void signIn_Success() throws Exception {

    SignInForm signInForm = SignInForm.builder()
        .email("test@test.com")
        .password("12345678")
        .build();

    userRepository.save(User.builder()
        .email(signInForm.getEmail())
        .password(passwordEncoder.encode(signInForm.getPassword()))
        .build());

    String signUpFormJson = objectMapper.writeValueAsString(signInForm);

    mockMvc.perform(
        post(url + "/v1/user/sign-in")
            .content(signUpFormJson)
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(
        status().isOk()
    ).andExpect(
        header().exists(AUTHORIZATION)
    ).andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("로그인 실패 - 가입된 유저 없음")
  @Test
  void signIn_Fail_NotFoundUser() throws Exception {

    SignInForm signInForm = SignInForm.builder()
        .email("test@test.com")
        .password("12345678")
        .build();

    String signUpFormJson = objectMapper.writeValueAsString(signInForm);

    mockMvc.perform(
        post(url + "/v1/user/sign-in")
            .content(signUpFormJson)
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(
        status().isBadRequest()
    ).andExpect(
        jsonPath("$.message", is(NOT_FOUND_USER.getDetail()))
    ).andExpect(
        jsonPath("$.code", Matchers.is(NOT_FOUND_USER.toString()))
    ).andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("로그인 실패 - 비밀번호 틀림")
  @Test
  void signIn_Fail_MismatchEmailOrPassword() throws Exception {

    SignInForm signInForm = SignInForm.builder()
        .email("test@test.com")
        .password("12345678")
        .build();

    userRepository.save(User.builder()
        .email(signInForm.getEmail())
        .password(passwordEncoder.encode(signInForm.getPassword() + 1))
        .build());

    String signUpFormJson = objectMapper.writeValueAsString(signInForm);

    mockMvc.perform(
        post(url + "/v1/user/sign-in")
            .content(signUpFormJson)
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(
        status().isBadRequest()
    ).andExpect(
        jsonPath("$.message", is(MISMATCH_EMAIL_OR_PASSWORD.getDetail()))
    ).andExpect(
        jsonPath("$.code", Matchers.is(MISMATCH_EMAIL_OR_PASSWORD.toString()))
    ).andDo(MockMvcResultHandlers.print());
  }


}
