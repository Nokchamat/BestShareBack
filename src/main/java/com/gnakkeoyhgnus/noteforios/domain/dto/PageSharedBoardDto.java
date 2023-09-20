package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class PageSharedBoardDto {

  private Long id;

  private String title;

  private String explains;

  private String thumbnailUrl;

  private String pagePdfFileKey;

  private LocalDateTime createdAt;

  private Long viewCount;

  private Long userId;

  private String userNickname;

  private Long likesCount;

  public static PageSharedBoardDto fromEntity(PageShareBoard pageShareBoard) {

    return PageSharedBoardDto.builder()
        .id(pageShareBoard.getId())
        .title(pageShareBoard.getTitle())
        .explains(pageShareBoard.getExplains())
        .thumbnailUrl(pageShareBoard.getThumbnailUrl())
        .pagePdfFileKey(pageShareBoard.getPagePDFUrl())
        .userId(pageShareBoard.getUser().getId())
        .viewCount(pageShareBoard.getViewCount())
        .createdAt(pageShareBoard.getCreatedAt())
        .userNickname("탈퇴한 유저")
        .build();
  }

}
