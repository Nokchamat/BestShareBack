package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageSharedBoardListDto {

  private Long id;

  private String title;

  private String thumbnailUrl;

  private Long viewCount;

  public static PageSharedBoardListDto fromEntity(PageShareBoard pageShareBoard) {

    return new PageSharedBoardListDto(pageShareBoard.getId(), pageShareBoard.getTitle(),
        pageShareBoard.getThumbnailUrl(), pageShareBoard.getViewCount());
  }

}
