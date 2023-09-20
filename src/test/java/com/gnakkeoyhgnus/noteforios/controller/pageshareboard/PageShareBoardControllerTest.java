package com.gnakkeoyhgnus.noteforios.controller.pageshareboard;

import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.NOT_FOUND_PAGE_SHARE_BOARD;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.PERMISSION_DENIED_TO_DELETE;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.WRITE_THE_EXPLAIN;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.WRITE_THE_TITLE;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.CreatePageShareBoardForm;
import com.gnakkeoyhgnus.noteforios.domain.form.UpdatePageShareBoardForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.PageShareBoardRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.jwt.JwtTokenProvider;
import com.gnakkeoyhgnus.noteforios.service.AmazonS3Service;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@DisplayName("속지 공유 게시판 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class PageShareBoardControllerTest {

  @MockBean
  private AmazonS3Service amazonS3Service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PageShareBoardRepository pageShareBoardRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private static final String url = "http://localhost:8080";
  private static final String UPLOADED_THUMBNAIL = "thumbnail";
  private static final String UPLOADED_PDF = "PDF";

  @AfterEach
  void init() {
    pageShareBoardRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("속지 공유 게시판 작성 - 성공")
  @Test
  void createPageShareBoard_Success() throws Exception {
    CreatePageShareBoardForm createPageShareBoardForm = CreatePageShareBoardForm.builder()
        .title("제목")
        .explains("설명")
        .build();

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    when(amazonS3Service.uploadForThumbnail(any(), any()))
        .thenReturn(UPLOADED_THUMBNAIL);
    when(amazonS3Service.uploadForPDF(any(), any()))
        .thenReturn(UPLOADED_PDF);

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
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    PageShareBoard pageShareBoard = pageShareBoardRepository.findAll().get(0);

    assertEquals(createPageShareBoardForm.getTitle(), pageShareBoard.getTitle());
    assertEquals(createPageShareBoardForm.getExplains(), pageShareBoard.getExplains());
    assertEquals(UPLOADED_THUMBNAIL, pageShareBoard.getThumbnailUrl());
    assertEquals(UPLOADED_PDF, pageShareBoard.getPagePDFUrl());
    assertEquals(0L, pageShareBoard.getViewCount());
  }

  @DisplayName("속지 공유 게시판 작성 - 실패 - Valid")
  @Test
  void createPageShareBoard_Fail() throws Exception {
    CreatePageShareBoardForm createPageShareBoardForm = CreatePageShareBoardForm.builder()
        .title("")
        .explains("")
        .build();

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    when(amazonS3Service.uploadForThumbnail(any(), any()))
        .thenReturn(UPLOADED_THUMBNAIL);
    when(amazonS3Service.uploadForPDF(any(), any()))
        .thenReturn(UPLOADED_PDF);

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
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isBadRequest()
        ).andExpect(
            jsonPath("$.messages", hasItem(WRITE_THE_TITLE))
        ).andExpect(
            jsonPath("$.messages", hasItem(WRITE_THE_EXPLAIN))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("속지 공유 게시판 리스트 조회 - 성공")
  @Test
  void getAllPageShareBoard_Success() throws Exception {
    pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .build());
    pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .build());

    mockMvc.perform(
            get(url + "/v1/pageshareboard")
        ).andExpect(
            status().isOk()
        ).andExpect(
            jsonPath("$.content", hasSize(2))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("속지 공유 게시판 상세 조회 - 성공")
  @Test
  void getDetailForPageShareBoardId() throws Exception {
    User user = userRepository.save(User.builder()
        .nickname("테스트 닉네임")
        .build());

    PageShareBoard pageShareBoard = pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .viewCount(0L)
        .pagePDFUrl(UPLOADED_PDF)
        .thumbnailUrl(UPLOADED_THUMBNAIL)
        .user(user)
        .build());

    mockMvc.perform(
            get(url + "/v1/pageshareboard/" + pageShareBoard.getId())
        ).andExpect(
            status().isOk()
        ).andExpect(
            jsonPath("$.title", Matchers.equalTo(pageShareBoard.getTitle()))
        ).andExpect(
            jsonPath("$.explains", Matchers.equalTo(pageShareBoard.getExplains()))
        ).andExpect(
            jsonPath("$.thumbnailUrl", Matchers.equalTo(pageShareBoard.getThumbnailUrl()))
        ).andExpect(
            jsonPath("$.pagePdfFileKey", Matchers.equalTo(pageShareBoard.getPagePDFUrl()))
        ).andExpect(
            jsonPath("$.userNickname", Matchers.equalTo(pageShareBoard.getUser().getNickname()))
        )
        .andDo(MockMvcResultHandlers.print());
  }


  @DisplayName("속지 공유 게시판 삭제 - 성공")
  @Test
  void deletePageShareBoard_Success() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    PageShareBoard pageShareBoard = pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .viewCount(0L)
        .pagePDFUrl(UPLOADED_PDF)
        .thumbnailUrl(UPLOADED_THUMBNAIL)
        .user(user)
        .build());

    mockMvc.perform(
            delete(url + "/v1/pageshareboard/" + pageShareBoard.getId())
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    assertTrue(pageShareBoardRepository.findAll().isEmpty());
  }

  @DisplayName("속지 공유 게시판 삭제 - 실패 - 권한 없음")
  @Test
  void deletePageShareBoard_Fail_PermissionDenied() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    User deniedUser = userRepository.save(User.builder()
        .email("test2@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    PageShareBoard pageShareBoard = pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .viewCount(0L)
        .pagePDFUrl(UPLOADED_PDF)
        .thumbnailUrl(UPLOADED_THUMBNAIL)
        .user(user)
        .build());

    mockMvc.perform(
            delete(url + "/v1/pageshareboard/" + pageShareBoard.getId())
                .header(HttpHeaders.AUTHORIZATION,
                    jwtTokenProvider.createAccessToken(deniedUser.getEmail()))
        ).andExpect(
            status().isForbidden()
        ).andExpect(
            jsonPath("$.message", is(PERMISSION_DENIED_TO_DELETE.getDetail()))
        ).andExpect(
            jsonPath("$.code", is(PERMISSION_DENIED_TO_DELETE.toString()))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("속지 공유 게시판 삭제 - 실패 - 게시판 없음")
  @Test
  void deletePageShareBoard_Fail_NotFoundBoard() throws Exception {
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    mockMvc.perform(
            delete(url + "/v1/pageshareboard/" + 1)
                .header(HttpHeaders.AUTHORIZATION,
                    jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isBadRequest()
        ).andExpect(
            jsonPath("$.message", is(NOT_FOUND_PAGE_SHARE_BOARD.getDetail()))
        ).andExpect(
            jsonPath("$.code", is(NOT_FOUND_PAGE_SHARE_BOARD.toString()))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("속지 공유 게시판 수정 - 성공")
  @Test
  void updatePageShareBoard_Success() throws Exception {
    UpdatePageShareBoardForm updatePageShareBoardForm = UpdatePageShareBoardForm.builder()
        .title("수정된 제목")
        .explains("수정된 설명")
        .build();

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    PageShareBoard pageShareBoard = pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .viewCount(0L)
        .pagePDFUrl("")
        .thumbnailUrl("")
        .user(user)
        .build());

    String updatePageShareBoardFormJson =
        objectMapper.writeValueAsString(updatePageShareBoardForm);
    MockMultipartFile form = new MockMultipartFile(
        "updatePageShareBoardForm", "updatePageShareBoardForm", "application/json",
        updatePageShareBoardFormJson.getBytes(StandardCharsets.UTF_8));
    MockMultipartFile thumbnail = new MockMultipartFile(
        "thumbnail", "thumbnail.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));
    MockMultipartFile pagePDF = new MockMultipartFile(
        "pagePDF", "pagePDF.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(
            multipart(HttpMethod.PUT, url + "/v1/pageshareboard/" + pageShareBoard.getId())
                .file(form)
                .file(thumbnail)
                .file(pagePDF)
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    PageShareBoard updatedBoard = pageShareBoardRepository.findAll().get(0);

    assertNotEquals(pageShareBoard.getTitle(), updatedBoard.getTitle());
    assertEquals(updatePageShareBoardForm.getTitle(), updatedBoard.getTitle());
    assertNotEquals(pageShareBoard.getExplains(), updatedBoard.getExplains());
    assertEquals(updatePageShareBoardForm.getExplains(), updatedBoard.getExplains());
  }

  @DisplayName("속지 공유 게시판 수정 - 실패 - 권한 없음")
  @Test
  void updatePageShareBoard_Fail_PermissionDenied() throws Exception {
    UpdatePageShareBoardForm updatePageShareBoardForm = UpdatePageShareBoardForm.builder()
        .title("수정된 제목")
        .explains("수정된 설명")
        .build();

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());
    User deniedUser = userRepository.save(User.builder()
        .email("test1@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    PageShareBoard pageShareBoard = pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .viewCount(0L)
        .pagePDFUrl("")
        .thumbnailUrl("")
        .user(user)
        .build());

    String updatePageShareBoardFormJson =
        objectMapper.writeValueAsString(updatePageShareBoardForm);
    MockMultipartFile form = new MockMultipartFile(
        "updatePageShareBoardForm", "updatePageShareBoardForm", "application/json",
        updatePageShareBoardFormJson.getBytes(StandardCharsets.UTF_8));
    MockMultipartFile thumbnail = new MockMultipartFile(
        "thumbnail", "thumbnail.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));
    MockMultipartFile pagePDF = new MockMultipartFile(
        "pagePDF", "pagePDF.jpeg", "text/plain",
        "test".getBytes(StandardCharsets.UTF_8));

    mockMvc.perform(
            multipart(HttpMethod.PUT, url + "/v1/pageshareboard/" + pageShareBoard.getId())
                .file(form)
                .file(thumbnail)
                .file(pagePDF)
                .header(HttpHeaders.AUTHORIZATION,
                    jwtTokenProvider.createAccessToken(deniedUser.getEmail()))
        ).andExpect(
            status().isForbidden()
        )
        .andDo(MockMvcResultHandlers.print());
  }

}
