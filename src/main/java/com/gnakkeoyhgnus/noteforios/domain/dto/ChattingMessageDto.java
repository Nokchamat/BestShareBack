package com.gnakkeoyhgnus.noteforios.domain.dto;

import com.gnakkeoyhgnus.noteforios.domain.entity.ChattingMessage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChattingMessageDto {

  private Long id;

  private Long chattingRoomId;

  private Long sender;

  private String message;

  private boolean isRead;

  private LocalDateTime createdAt;

  public static ChattingMessageDto fromEntity(ChattingMessage chattingMessage) {
    return ChattingMessageDto.builder()
        .id(chattingMessage.getId())
        .chattingRoomId(chattingMessage.getChattingRoom().getId())
        .sender(chattingMessage.getSender().getId())
        .message(chattingMessage.getMessage())
        .isRead(chattingMessage.getIsRead())
        .createdAt(chattingMessage.getCreatedAt())
        .build();
  }

}
