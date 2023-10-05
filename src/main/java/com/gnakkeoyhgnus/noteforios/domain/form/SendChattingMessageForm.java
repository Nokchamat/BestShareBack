package com.gnakkeoyhgnus.noteforios.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SendChattingMessageForm {

  private Long chattingRoomId;

  private Long senderId;

  private String message;

}
