package com.gnakkeoyhgnus.noteforios.service;

import com.gnakkeoyhgnus.noteforios.domain.dto.NotificationKeywordsDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.NotificationKeywords;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.NotificationKeywordsRepository;
import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationKeywordsService {

  private final NotificationKeywordsRepository notificationKeywordsRepository;

  public void addNotificationKeywords(User user, String keyword) {
    notificationKeywordsRepository.save(NotificationKeywords.builder()
        .user(user)
        .keyword(keyword)
        .build());
  }

  public void deleteNotificationKeywords(User user, Long notificationKeywordsId) {
    NotificationKeywords notificationKeywords = notificationKeywordsRepository.findById(
            notificationKeywordsId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTIFICATIONKEYWORDS));

    if (Objects.equals(notificationKeywords.getUser().getId(), user.getId())) {
      throw new CustomException(ErrorCode.PERMISSION_DENIED_TO_DELETE);
    }

    notificationKeywordsRepository.delete(notificationKeywords);
  }

  public Page<NotificationKeywordsDto> getAllNotificationKeywords(User user, Pageable pageable) {
    return notificationKeywordsRepository.findAllByUserId(user.getId(), pageable)
        .map(NotificationKeywordsDto::fromEntity);
  }


}
