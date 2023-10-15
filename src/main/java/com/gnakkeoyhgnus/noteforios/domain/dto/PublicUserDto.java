package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicUserDto {

  private Long id;

  private String nickname;

  private String profileImageUrl;

  private Long followerCount;

  private Long followId;

  private Boolean isFollow;

  public static PublicUserDto fromEntity(User user) {
    return PublicUserDto.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .profileImageUrl(user.getProfileImageUrl())
        .followerCount(0L)
        .isFollow(false)
        .build();
  }

}
