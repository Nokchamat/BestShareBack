package com.gnakkeoyhgnus.noteforios.service;

import com.gnakkeoyhgnus.noteforios.jwt.JwtTokenProvider;
import com.gnakkeoyhgnus.noteforios.domain.constants.RoleType;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.SignInForm;
import com.gnakkeoyhgnus.noteforios.domain.form.SignUpForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AmazonS3Service amazonS3Service;
  private final JwtTokenProvider jwtTokenProvider;

  @Value("${profile.image.default}")
  private String defaultProfileImage;

  public void signUp(SignUpForm signUpForm, MultipartFile profileImage) {
    log.info("[signUp] 회원가입 시작 Email : " + signUpForm.getEmail());

    userRepository.findByEmail(signUpForm.getEmail())
        .ifPresent(user -> {
          throw new CustomException(ErrorCode.ALREADY_EXISTS_EMAIL);
        });
    userRepository.findByNickname(signUpForm.getNickname())
        .ifPresent(user -> {
          throw new CustomException(ErrorCode.ALREADY_EXISTS_NICKNAME);
        });

    log.info("[signUp] 회원가입 완료 Email : " + signUpForm.getEmail());
    userRepository.save(User.builder()
        .email(signUpForm.getEmail())
        .password(passwordEncoder.encode(signUpForm.getPassword()))
        .name(signUpForm.getName())
        .nickname(signUpForm.getNickname())
        .phoneNumber(signUpForm.getPhoneNumber())
        .profileImageUrl(profileImage.isEmpty() ?
            defaultProfileImage : amazonS3Service.uploadForProfile(profileImage, signUpForm.getEmail())
        )
        .role(RoleType.USER)
        .emailVerified(false)
        .emailVerifiedCode(RandomString.make(5))
        .build());
  }

  public String signIn(SignInForm signInForm) {
    log.info("[signIn] 로그인 시작 Email : " + signInForm.getEmail());

    User user = userRepository.findByEmail(signInForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    if (!passwordEncoder.matches(signInForm.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.MISMATCH_EMAIL_OR_PASSWORD);
    }

    log.info("[signIn] 로그인 완료 Email : " + signInForm.getEmail());
    return jwtTokenProvider.createAccessToken(signInForm.getEmail());
  }

}
