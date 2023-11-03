package com.gnakkeoyhgnus.noteforios.domain.entity;

import com.gnakkeoyhgnus.noteforios.domain.constants.RoleType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;

  private String name;

  private String nickname;

  private String phoneNumber;

  private String profileImageUrl;

  private Boolean emailVerified;

  private String emailVerifiedCode;

  @Enumerated(EnumType.STRING)
  private RoleType role;

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void setEmailVerified() {
    this.emailVerified = true;
  }

}
