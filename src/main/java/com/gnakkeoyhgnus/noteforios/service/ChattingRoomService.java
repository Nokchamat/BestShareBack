package com.gnakkeoyhgnus.noteforios.service;

import com.gnakkeoyhgnus.noteforios.domain.dto.ChattingRoomDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.ChattingRoom;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.ChattingRoomRepository;
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
public class ChattingRoomService {

  private final ChattingRoomRepository chattingRoomRepository;

  private final UserRepository userRepository;

  @Transactional
  public void createChattingRoom(User user, Long userId) {
    User secondUser = userRepository.findById(userId).orElseThrow(
        () -> new CustomException(ErrorCode.NOT_FOUND_USER));

    chattingRoomRepository.findByFirstUserIdAndSecondUserId(user.getId(), userId)
        .ifPresent(chattingRoom -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_CHATTINGROOM);
        });

    chattingRoomRepository.findByFirstUserIdAndSecondUserId(userId, user.getId())
        .ifPresent(chattingRoom -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_CHATTINGROOM);
        });

    chattingRoomRepository.save(ChattingRoom.builder()
        .firstUser(user)
        .secondUser(secondUser)
        .firstUserExist(true)
        .secondUserExist(true)
        .build());
  }

  @Transactional
  public void leaveChattingRoom(User user, Long chattingroomId) {
    ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingroomId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM));

    if (Objects.equals(chattingRoom.getFirstUser().getId(), user.getId()) &&
        chattingRoom.getFirstUserExist()) {

      chattingRoom.setLeaveFirstUser();
    } else if (Objects.equals(chattingRoom.getSecondUser().getId(), user.getId()) &&
        chattingRoom.getSecondUserExist()) {

      chattingRoom.setLeaveSecondUser();
    }

    if (!chattingRoom.existUser()) {
      chattingRoomRepository.delete(chattingRoom);
    }

  }

  public Page<ChattingRoomDto> getChattingRoom(User user, Pageable pageable) {

    return chattingRoomRepository.findAllByFirstUserIdAndFirstUserExistIsTrueOrSecondUserIdAndSecondUserExistIsTrue(user.getId(), user.getId(), pageable)
        .map(chattingRoom -> {
          if (Objects.equals(chattingRoom.getFirstUser().getId(), user.getId())) {
            return ChattingRoomDto.builder()
                .id(chattingRoom.getId())
                .userNickname(chattingRoom.getSecondUser().getNickname())
                .createdAt(chattingRoom.getCreatedAt())
                .build();
          } else {
            return ChattingRoomDto.builder()
                .id(chattingRoom.getId())
                .userNickname(chattingRoom.getFirstUser().getNickname())
                .createdAt(chattingRoom.getCreatedAt())
                .build();
          }
        });
  }
}
