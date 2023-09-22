package com.gnakkeoyhgnus.noteforios.controller.notificationkeywords;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gnakkeoyhgnus.noteforios.domain.constants.RoleType;
import com.gnakkeoyhgnus.noteforios.domain.entity.NotificationKeywords;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.NotificationKeywordsRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.PageShareBoardRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.jwt.JwtTokenProvider;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@DisplayName("알림 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class NotificationKeywordsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private NotificationKeywordsRepository notificationKeywordsRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PageShareBoardRepository pageShareBoardRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private static final String url = "http://localhost:8080";

  @AfterEach
  void init() {
    notificationKeywordsRepository.deleteAll();
    pageShareBoardRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("알림 조회 - 성공")
  @Test
  void getMyNotificationKeywords_Success() throws Exception {

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    notificationKeywordsRepository.save(NotificationKeywords.builder()
        .user(user)
        .keyword("게시물1 작성")
        .build());
    notificationKeywordsRepository.save(NotificationKeywords.builder()
        .user(user)
        .keyword("게시물2 작성")
        .build());

    mockMvc.perform(
            get(url + "/v1/notificationkeywords")
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isOk()
        ).andExpect(
            jsonPath("$.content", hasSize(2))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("알림 삭제 - 성공")
  @Test
  void deleteNotificationKeywords_Success() throws Exception {

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    NotificationKeywords notificationKeywords = notificationKeywordsRepository.save(
        NotificationKeywords.builder()
            .user(user)
            .keyword("게시물1 작성")
            .build());

    mockMvc.perform(
            delete(url + "/v1/notificationkeywords/" + notificationKeywords.getId())
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    assertTrue(notificationKeywordsRepository
        .findAllByUserId(user.getId(), Pageable.unpaged()).isEmpty());
  }

}
