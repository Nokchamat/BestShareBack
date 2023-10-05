package com.gnakkeoyhgnus.noteforios.service;

import com.gnakkeoyhgnus.noteforios.domain.dto.ChattingMessageDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.ChattingMessage;
import com.gnakkeoyhgnus.noteforios.domain.entity.ChattingRoom;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.SendChattingMessageForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.ChattingMessageRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.ChattingRoomRepository;
import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingMessageService {

  private final ChattingMessageRepository chattingMessageRepository;

  private final ChattingRoomRepository chattingRoomRepository;

  @Transactional
  public ChattingMessageDto sendChattingMessage(User user,
      SendChattingMessageForm sendChattingMessageForm) {

    ChattingRoom chattingRoom = chattingRoomRepository.findById(
            sendChattingMessageForm.getChattingRoomId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM));

    ChattingMessage chattingMessage = chattingMessageRepository.save(ChattingMessage.builder()
        .chattingRoom(chattingRoom)
        .sender(user)
        .message(sendChattingMessageForm.getMessage())
        .isRead(false)
        .build());

    return ChattingMessageDto.fromEntity(chattingMessage);
  }

  @Transactional
  public Page<ChattingMessageDto> getAllChattingMessage(
      User user, Long chattingRoomId, Pageable pageable) {

    chattingRoomRepository.findById(chattingRoomId)
        .ifPresent(chattingRoom -> {
          if (!Objects.equals(chattingRoom.getFirstUser().getId(), user.getId()) &&
              !Objects.equals(chattingRoom.getSecondUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
          }
        });

    return chattingMessageRepository.findAllByChattingRoomId(chattingRoomId, pageable)
        .map(ChattingMessageDto::fromEntity);
//        .map(chattingMessage -> {
//          if (!Objects.equals(user.getId(), chattingMessage.getSender().getId())) {
//            chattingMessage.setIsRead();
//          }
//
//          return ChattingMessageDto.fromEntity(chattingMessage);
//        });
  }

}
