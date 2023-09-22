package com.gnakkeoyhgnus.noteforios.controller.notificationkeywords;

import static java.lang.String.format;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnakkeoyhgnus.noteforios.domain.constants.RoleType;
import com.gnakkeoyhgnus.noteforios.domain.entity.Follow;
import com.gnakkeoyhgnus.noteforios.domain.entity.NotificationKeywords;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.CreatePageShareBoardForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.FollowRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.NotificationKeywordsRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.PageShareBoardRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.jwt.JwtTokenProvider;
import com.gnakkeoyhgnus.noteforios.service.AmazonS3Service;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@DisplayName("알림 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class NotificationKeywordsControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AmazonS3Service amazonS3Service;

  @Autowired
  private NotificationKeywordsRepository notificationKeywordsRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PageShareBoardRepository pageShareBoardRepository;

  @Autowired
  private FollowRepository followRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private static final String url = "http://localhost:8080";

  @AfterEach
  void init() {
    notificationKeywordsRepository.deleteAll();
    followRepository.deleteAll();
    pageShareBoardRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("알림 추가 - 성공")
  @Test
  void addMyNotificationKeywords_Success() throws Exception {

    CreatePageShareBoardForm createPageShareBoardForm = CreatePageShareBoardForm.builder()
        .title("제목")
        .explains("설명")
        .build();

    User following = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());
    User follower1 = userRepository.save(User.builder()
        .email("test1@test.com")
        .password("12341234")
        .nickname("닉네임1")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());
    User follower2 = userRepository.save(User.builder()
        .email("test2@test.com")
        .password("12341234")
        .nickname("닉네임2")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    followRepository.save(Follow.builder()
        .following(following)
        .follower(follower1)
        .build());
    followRepository.save(Follow.builder()
        .following(following)
        .follower(follower2)
        .build());

    when(amazonS3Service.uploadForThumbnail(any(), any()))
        .thenReturn("thumbnail");
    when(amazonS3Service.uploadForPDF(any(), any()))
        .thenReturn("pdf");

    String createPageShareBoardFormJson =
        objectMapper.writeValueAsString(createPageShareBoardForm);

    MockMultipartFile form = new MockMultipartFile(
        "createPageShareBoardForm", "createPageShareBoardForm", "application/json",
        createPageShareBoardFormJson.getBytes(StandardCharsets.UTF_8));
    MockMultipartFile thumbnail = new MockMultipartFile(
        "thumbnail", "thumbnail.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));
    MockMultipartFile pagePDF = new MockMultipartFile(
        "pagePDF", "pagePDF.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(
            multipart(url + "/v1/pageshareboard")
                .file(form)
                .file(thumbnail)
                .file(pagePDF)
                .header(HttpHeaders.AUTHORIZATION,
                    jwtTokenProvider.createAccessToken(following.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    List<NotificationKeywords> notiList = notificationKeywordsRepository.findAll();

    assertEquals(2, notiList.size());
    assertEquals(format("%s님의 게시물이 올라왔습니다.", following.getNickname())
        , notiList.get(0).getKeyword());
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
