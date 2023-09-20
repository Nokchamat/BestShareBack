package com.gnakkeoyhgnus.noteforios.controller.user;

import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.ALREADY_EXISTS_EMAIL;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.ALREADY_EXISTS_NICKNAME;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.CHECK_THE_EMAIL;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.CHECK_THE_NICKNAME_MIN1_MAX10;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.CHECK_THE_PHONE_NUMBER;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.ONLY_NUMBER;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.PASSWORD_TOO_SHORT_MIN5;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.WRITE_THE_NAME;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnakkeoyhgnus.noteforios.domain.constants.RoleType;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.SignUpForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import com.gnakkeoyhgnus.noteforios.service.AmazonS3Service;
import java.nio.charset.StandardCharsets;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@DisplayName("유저 컨트롤러 - 회원가입 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerSignUpTest {

  @MockBean
  private AmazonS3Service amazonS3Service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  private static final String url = "http://localhost:8080";

  @AfterEach
  void init() {
    userRepository.deleteAll();
  }

  @DisplayName("회원가입 - 성공 - 프로필 첨부")
  @Test
  void signUp_Success_ImageUpload() throws Exception {

    SignUpForm signUpForm = SignUpForm.builder()
        .email("test@test.com")
        .password("12345678")
        .name("테스트")
        .nickname("닉네임")
        .phoneNumber("01012341234")
        .build();

    when(amazonS3Service.uploadForProfile(any(), any()))
        .thenReturn("uploadedImage");

    String signUpFormJson = objectMapper.writeValueAsString(signUpForm);
    MockMultipartFile form = new MockMultipartFile(
        "signUpForm", "signUpForm", "application/json",
        signUpFormJson.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile image = new MockMultipartFile(
        "profileImage", "imageFile.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(
            multipart(url + "/v1/user/sign-up")
                .file(image)
                .file(form)
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    User user = userRepository.findByEmail(signUpForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    assertEquals(user.getEmail(), signUpForm.getEmail());
    assertEquals(user.getName(), signUpForm.getName());
    assertEquals(user.getNickname(), signUpForm.getNickname());
    assertEquals(user.getPhoneNumber(), signUpForm.getPhoneNumber());
    assertEquals(user.getRole(), RoleType.USER);
    assertEquals(user.getProfileImageUrl(), "uploadedImage");

  }

  @DisplayName("회원가입 - 성공 - 프로필 미첨부")
  @Test
  void signUp_Success_NoneImageUpload() throws Exception {

    SignUpForm signUpForm = SignUpForm.builder()
        .email("test@test.com")
        .password("12345678")
        .name("테스트")
        .nickname("닉네임")
        .phoneNumber("01012341234")
        .build();

    when(amazonS3Service.uploadForProfile(any(), any()))
        .thenReturn("uploadedImage");

    String signUpFormJson = objectMapper.writeValueAsString(signUpForm);
    MockMultipartFile form = new MockMultipartFile(
        "signUpForm", "signUpForm", "application/json",
        signUpFormJson.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile image = new MockMultipartFile(
        "profileImage", "", "text/plain",
        "".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(
        multipart(url + "/v1/user/sign-up")
            .file(image)
            .file(form)
    ).andExpect(
        status().isOk()
    ).andDo(MockMvcResultHandlers.print());

    User user = userRepository.findByEmail(signUpForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    assertEquals(user.getEmail(), signUpForm.getEmail());
    assertEquals(user.getName(), signUpForm.getName());
    assertEquals(user.getNickname(), signUpForm.getNickname());
    assertEquals(user.getPhoneNumber(), signUpForm.getPhoneNumber());
    assertEquals(user.getRole(), RoleType.USER);
    assertEquals(user.getProfileImageUrl(), "testDefaultImageUrl");
  }

  @DisplayName("회원가입 - 실패 - SignUpForm Valid1")
  @Test
  void signUp_Fail1() throws Exception {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("test")
        .password("1234")
        .name("")
        .nickname("")
        .phoneNumber("")
        .build();

    String signUpFormJson = objectMapper.writeValueAsString(signUpForm);
    MockMultipartFile form = new MockMultipartFile(
        "signUpForm", "signUpForm", "application/json",
        signUpFormJson.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile image = new MockMultipartFile(
        "profileImage", "imageFile.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));

    //when
    mockMvc.perform(
        multipart(url + "/v1/user/sign-up")
            .file(image)
            .file(form)
        //then
    ).andExpect(
        status().isBadRequest()
    ).andExpect(
        jsonPath("$.messages", hasItem(CHECK_THE_EMAIL))
    ).andExpect(
        jsonPath("$.messages", hasItem(PASSWORD_TOO_SHORT_MIN5))
    ).andExpect(
        jsonPath("$.messages", hasItem(WRITE_THE_NAME))
    ).andExpect(
        jsonPath("$.messages", hasItem(CHECK_THE_NICKNAME_MIN1_MAX10))
    ).andExpect(
        jsonPath("$.messages", hasItem(CHECK_THE_PHONE_NUMBER))
    ).andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("회원가입 - 실패 - SignUpForm Valid2")
  @Test
  void signUp_Fail2() throws Exception {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("test@naver.com")
        .password("12345")
        .name("이름")
        .nickname("닉네임")
        .phoneNumber("0101234123x")
        .build();

    String signUpFormJson = objectMapper.writeValueAsString(signUpForm);
    MockMultipartFile form = new MockMultipartFile(
        "signUpForm", "signUpForm", "application/json",
        signUpFormJson.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile image = new MockMultipartFile(
        "profileImage", "imageFile.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));

    //when
    mockMvc.perform(
        multipart(url + "/v1/user/sign-up")
            .file(image)
            .file(form)
        //then
    ).andExpect(
        status().isBadRequest()
    ).andExpect(
        jsonPath("$.messages", hasItem(ONLY_NUMBER))
    ).andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("회원가입 - 실패 - 이메일 중복")
  @Test
  void signUp_Fail_AlreadyExistEmail() throws Exception {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("test@naver.com")
        .password("12345")
        .name("이름")
        .nickname("닉네임")
        .phoneNumber("01012341234")
        .build();

    userRepository.save(User.builder()
        .email(signUpForm.getEmail())
        .password(signUpForm.getEmail())
        .build());

    String signUpFormJson = objectMapper.writeValueAsString(signUpForm);
    MockMultipartFile form = new MockMultipartFile(
        "signUpForm", "signUpForm", "application/json",
        signUpFormJson.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile image = new MockMultipartFile(
        "profileImage", "imageFile.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));

    //when
    mockMvc.perform(
        multipart(url + "/v1/user/sign-up")
            .file(image)
            .file(form)
        //then
    ).andExpect(
        status().isBadRequest()
    ).andExpect(
        jsonPath("$.message", Matchers.is(ALREADY_EXISTS_EMAIL.getDetail()))
    ).andExpect(
        jsonPath("$.code", Matchers.is(ALREADY_EXISTS_EMAIL.toString()))
    ).andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("회원가입 - 실패 - 닉네임 중복")
  @Test
  void signUp_Fail_AlreadyExistNickname() throws Exception {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("test@naver.com")
        .password("12345")
        .name("이름")
        .nickname("중복닉네임")
        .phoneNumber("01012341234")
        .build();

    userRepository.save(User.builder()
        .email(signUpForm.getEmail() + "test")
        .password(signUpForm.getEmail())
        .nickname(signUpForm.getNickname())
        .build());

    String signUpFormJson = objectMapper.writeValueAsString(signUpForm);
    MockMultipartFile form = new MockMultipartFile(
        "signUpForm", "signUpForm", "application/json",
        signUpFormJson.getBytes(StandardCharsets.UTF_8));

    MockMultipartFile image = new MockMultipartFile(
        "profileImage", "imageFile.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));

    //when
    mockMvc.perform(
        multipart(url + "/v1/user/sign-up")
            .file(image)
            .file(form)
        //then
    ).andExpect(
        status().isBadRequest()
    ).andExpect(
        jsonPath("$.message", Matchers.is(ALREADY_EXISTS_NICKNAME.getDetail()))
    ).andExpect(
        jsonPath("$.code", Matchers.is(ALREADY_EXISTS_NICKNAME.toString()))
    ).andDo(MockMvcResultHandlers.print());
  }

}
