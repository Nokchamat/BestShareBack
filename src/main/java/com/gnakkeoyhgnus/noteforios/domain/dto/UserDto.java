package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  private Long id;

  private String email;

  private String name;

  private String nickname;

  private String phoneNumber;

  private String profileImageUrl;

  public static UserDto fromEntity(User user) {
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .nickname(user.getNickname())
        .phoneNumber(user.getPhoneNumber())
        .profileImageUrl(user.getProfileImageUrl())
        .build();
  }

}
