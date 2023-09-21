package com.gnakkeoyhgnus.noteforios.domain.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FollowDto {

  private Long id;

  private Long followingId;

  private String followingNickname;

  private String followingProfileUrl;

  private LocalDateTime createdAt;

}
