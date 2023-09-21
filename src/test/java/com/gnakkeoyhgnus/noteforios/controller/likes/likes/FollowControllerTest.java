package com.gnakkeoyhgnus.noteforios.controller.likes.likes;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gnakkeoyhgnus.noteforios.domain.constants.RoleType;
import com.gnakkeoyhgnus.noteforios.domain.entity.Follow;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.FollowRepository;
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

@DisplayName("팔로우 컨트롤러 테스트")
@AutoConfigureMockMvc
@SpringBootTest
public class FollowControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FollowRepository followRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private static final String url = "http://localhost:8080";

  @AfterEach
  void init() {
    followRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("팔로우 등록 - 성공")
  @Test
  void addFollow_Success() throws Exception {

    User following = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    User follower = userRepository.save(User.builder()
        .email("test1@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    mockMvc.perform(
            post(url + "/v1/user/" + following.getId() + "/follow")
                .header(HttpHeaders.AUTHORIZATION,
                    jwtTokenProvider.createAccessToken(follower.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    Follow follow = followRepository.findAll().get(0);
    assertEquals(follower.getId(), follow.getFollower().getId());
    assertEquals(following.getId(), follow.getFollowing().getId());
  }

  @DisplayName("팔로우 조회 - 성공")
  @Test
  void getAllMyFollow_Success() throws Exception {

    User follower = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    User following1 = userRepository.save(User.builder()
        .email("test1@test.com")
        .password("12341234")
        .nickname("팔로워1")
        .profileImageUrl("팔로워1썸내일")
        .role(RoleType.USER)
        .build());
    User following2 = userRepository.save(User.builder()
        .email("test2@test.com")
        .password("12341234")
        .nickname("팔로워2")
        .profileImageUrl("팔로워2썸내일")
        .role(RoleType.USER)
        .build());

    followRepository.save(Follow.builder()
        .follower(follower)
        .following(following1)
        .build());
    followRepository.save(Follow.builder()
        .follower(follower)
        .following(following2)
        .build());

    mockMvc.perform(
            get(url + "/v1/follow")
                .header(HttpHeaders.AUTHORIZATION,
                    jwtTokenProvider.createAccessToken(follower.getEmail()))
        ).andExpect(
            status().isOk()
        ).andExpect(
            jsonPath("$.content", hasSize(2))
        )
        .andDo(MockMvcResultHandlers.print());
  }

  @DisplayName("팔로우 삭제 - 성공")
  @Test
  void deleteFollow_Success() throws Exception {

    User follower = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .nickname("닉네임")
        .profileImageUrl("profileUrl")
        .role(RoleType.USER)
        .build());

    User following = userRepository.save(User.builder()
        .email("test1@test.com")
        .password("12341234")
        .nickname("팔로워1")
        .profileImageUrl("팔로워1썸내일")
        .role(RoleType.USER)
        .build());

    Follow follow = followRepository.save(Follow.builder()
        .follower(follower)
        .following(following)
        .build());

    mockMvc.perform(
            delete(url + "/v1/follow/" + follow.getId())
                .header(HttpHeaders.AUTHORIZATION,
                    jwtTokenProvider.createAccessToken(follower.getEmail()))
        ).andExpect(
            status().isOk()
        )
        .andDo(MockMvcResultHandlers.print());

    assertTrue(followRepository.findById(follow.getId()).isEmpty());
  }

}