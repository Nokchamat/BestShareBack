package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PageSharedBoardListDto {

  private Long id;

  private String title;

  private String thumbnailUrl;

  private Long viewCount;

  private Long likesCount;

  public static PageSharedBoardListDto fromEntity(PageShareBoard pageShareBoard) {
    return PageSharedBoardListDto.builder()
        .id(pageShareBoard.getId())
        .title(pageShareBoard.getTitle())
        .thumbnailUrl(pageShareBoard.getThumbnailUrl())
        .viewCount(pageShareBoard.getViewCount())
        .build();
  }

  public void setLikesCount(Long likesCount) {
    this.likesCount = likesCount;
  }

}
