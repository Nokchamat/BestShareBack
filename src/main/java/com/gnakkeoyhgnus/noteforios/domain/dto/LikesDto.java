package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikesDto {

  private Long id;

  private Long pageShareBoardId;

  private String title;

  private String thumbnailUrl;

  private Long viewCount;

  private Long likesCount;

  public static LikesDto fromEntity(PageShareBoard pageShareBoard, Long likesId) {
    return LikesDto.builder()
        .id(likesId)
        .pageShareBoardId(pageShareBoard.getId())
        .title(pageShareBoard.getTitle())
        .thumbnailUrl(pageShareBoard.getThumbnailUrl())
        .viewCount(pageShareBoard.getViewCount())
        .build();
  }

  public void setLikesCount(Long likesCount) {
    this.likesCount = likesCount;
  }

}
