package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.NotificationKeywords;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationKeywordsDto {

  private Long id;

  private String keyword;

  private LocalDateTime createdAt;

  public static NotificationKeywordsDto fromEntity(NotificationKeywords notificationKeywords) {
    return NotificationKeywordsDto.builder()
        .id(notificationKeywords.getId())
        .keyword(notificationKeywords.getKeyword())
        .createdAt(notificationKeywords.getCreatedAt())
        .build();
  }

}
