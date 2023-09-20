package com.gnakkeoyhgnus.noteforios.controller.likes;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gnakkeoyhgnus.noteforios.domain.constants.RoleType;
import com.gnakkeoyhgnus.noteforios.domain.entity.Likes;
import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.LikesRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@DisplayName("좋아요 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class LikesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PageShareBoardRepository pageShareBoardRepository;

  @Autowired
  private LikesRepository likesRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private static final String url = "http://localhost:8080";

  @AfterEach
  void init() {
    likesRepository.deleteAll();
    pageShareBoardRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("좋아요 등록 - 성공")
  @Test
  void addLikes_Success() throws Exception {

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
        .pagePDFUrl("pdf")
        .thumbnailUrl("thumbnail")
        .user(user)
        .build());

    mockMvc.perform(
            post(url + "/v1/pageshareboard/" + pageShareBoard.getId() + "/likes")
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    assertTrue(likesRepository.findByUserIdAndPageShareBoardId(
        user.getId(), pageShareBoard.getId()).isPresent());
  }

  @DisplayName("좋아요 조회 - 성공")
  @Test
  void getMyLikes_Success() throws Exception {

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    PageShareBoard pageShareBoard1 = pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .viewCount(0L)
        .pagePDFUrl("pdf")
        .thumbnailUrl("thumbnail")
        .user(user)
        .build());
    PageShareBoard pageShareBoard2 = pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .viewCount(0L)
        .pagePDFUrl("pdf")
        .thumbnailUrl("thumbnail")
        .user(user)
        .build());

    likesRepository.save(Likes.builder()
        .pageShareBoard(pageShareBoard1)
        .user(user)
        .build());
    likesRepository.save(Likes.builder()
        .pageShareBoard(pageShareBoard2)
        .user(user)
        .build());

    mockMvc.perform(
            get(url + "/v1/likes")
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isOk()
        ).andExpect(
            jsonPath("$.content", hasSize(2))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("좋아요 삭제 - 성공")
  @Test
  void deleteLikes_Success() throws Exception {

    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    PageShareBoard pageShareBoard1 = pageShareBoardRepository.save(PageShareBoard.builder()
        .title("제목")
        .explains("설명")
        .viewCount(0L)
        .pagePDFUrl("pdf")
        .thumbnailUrl("thumbnail")
        .user(user)
        .build());

    Likes likes = likesRepository.save(Likes.builder()
        .pageShareBoard(pageShareBoard1)
        .user(user)
        .build());

    mockMvc.perform(
            delete(url + "/v1/likes/" + likes.getId())
                .header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createAccessToken(user.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    assertTrue(likesRepository.findById(likes.getId()).isEmpty());
  }

}