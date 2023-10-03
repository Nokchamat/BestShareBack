package com.gnakkeoyhgnus.noteforios.domain.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChattingRoomDto {

  private Long id;

  private String userNickname;

  private LocalDateTime createdAt;

}
