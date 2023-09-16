package com.gnakkeoyhgnus.noteforios.service;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.gnakkeoyhgnus.noteforios.domain.constants.RoleType;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.SignUpForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AmazonS3Service amazonS3Service;

  @Value("${profile.image.default}")
  private String defaultProfileImage;

  public void signUp(SignUpForm signUpForm, MultipartFile profileImage) {

    userRepository.findByEmail(signUpForm.getEmail())
        .ifPresent(user -> {
          throw new AlreadyExistsException("이미 아이디가 존재합니다.");
        });

    userRepository.findByNickname(signUpForm.getNickname())
        .ifPresent(user -> {
          throw new AlreadyExistsException("이미 닉네임이 존재합니다.");
        });

    userRepository.save(User.builder()
        .email(signUpForm.getEmail())
        .password(passwordEncoder.encode(signUpForm.getPassword()))
        .name(signUpForm.getName())
        .nickname(signUpForm.getNickname())
        .phoneNumber(signUpForm.getPhoneNumber())
        .profileImageUrl(profileImage.isEmpty() ?
            defaultProfileImage : amazonS3Service.uploadImage(profileImage, signUpForm.getEmail())
        )
        .role(RoleType.USER)
        .emailVerified(false)
        .emailVerifiedCode(RandomString.make(5))
        .build());
  }

}
