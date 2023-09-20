package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.Likes;
import java.time.LocalDateTime;
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

  private LocalDateTime createdAt;

  public static LikesDto fromEntity(Likes likes) {
    return LikesDto.builder()
        .id(likes.getId())
        .pageShareBoardId(likes.getPageShareBoard().getId())
        .createdAt(likes.getCreatedAt())
        .build();
  }

}
