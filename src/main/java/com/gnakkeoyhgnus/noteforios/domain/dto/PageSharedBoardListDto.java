package com.gnakkeoyhgnus.noteforios.domain.dto;

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

}
