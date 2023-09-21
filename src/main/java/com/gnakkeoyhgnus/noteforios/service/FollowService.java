package com.gnakkeoyhgnus.noteforios.service;

import com.gnakkeoyhgnus.noteforios.domain.dto.FollowDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.Follow;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.FollowRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;

  private final UserRepository userRepository;

  @Transactional
  public void addFollow(Long followingUserId, User user) {
    User following = userRepository.findById(followingUserId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    followRepository.findByFollowerIdAndFollowingId(user.getId(), followingUserId)
        .ifPresent(follow -> {
          throw new CustomException(ErrorCode.ALREADY_ADD_FOLLOW);
        });

    followRepository.save(Follow.builder()
        .following(following)
        .follower(user)
        .build());
  }

  public Page<FollowDto> getAllMyFollow(User user, Pageable pageable) {
    return followRepository.findAllByFollowerId(user.getId(), pageable)
        .map(follow -> FollowDto.builder()
            .id(follow.getId())
            .followingId(follow.getFollowing().getId())
            .followingNickname(follow.getFollowing().getNickname())
            .followingProfileUrl(follow.getFollowing().getProfileImageUrl())
            .createdAt(follow.getCreatedAt())
            .build());
  }

  @Transactional
  public void deleteFollow(Long followId, User user) {
    Follow follow = followRepository.findById(followId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FOLLOW));

    if (!Objects.equals(user.getId(), follow.getFollower().getId())) {
      throw new CustomException(ErrorCode.PERMISSION_DENIED_TO_DELETE);
    }

    followRepository.delete(follow);
  }

}
