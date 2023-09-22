package com.gnakkeoyhgnus.noteforios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gnakkeoyhgnus.noteforios.domain.entity.NotificationKeywords;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.NotificationKeywordsRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("알림 서비스 테스트")
public class NotificationKeywordsServiceTest {

  @Autowired
  private NotificationKeywordsService notificationKeywordsService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private NotificationKeywordsRepository notificationKeywordsRepository;

  @AfterEach
  void init() {
    notificationKeywordsRepository.deleteAll();
    userRepository.deleteAll();
  }

  @DisplayName("알림 추가 - 성공")
  @Test
  void addNotificationKeywords_Success() {
    //given
    User user = userRepository.save(User.builder()
        .email("test@test.com")
        .password("12341234")
        .build());

    //when
    notificationKeywordsService.addNotificationKeywords(user, "알림 키워드");
    NotificationKeywords notificationKeywords = notificationKeywordsRepository.findAll().get(0);

    //then
    assertEquals("알림 키워드", notificationKeywords.getKeyword());
    assertEquals(user.getId(), notificationKeywords.getUser().getId());
  }

}
