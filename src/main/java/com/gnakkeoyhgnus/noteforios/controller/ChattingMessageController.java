package com.gnakkeoyhgnus.noteforios.controller;


import com.gnakkeoyhgnus.noteforios.domain.dto.ChattingMessageDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.SendChattingMessageForm;
import com.gnakkeoyhgnus.noteforios.service.ChattingMessageService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChattingMessageController {

  private final ChattingMessageService chattingMessageService;

  private final SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/chattingroom/{chattingRoomId}")
  public void sendChattingMessage(SimpMessageHeaderAccessor accessor,
      @DestinationVariable Long chattingRoomId,
      SendChattingMessageForm sendChattingMessageForm) {

    simpMessagingTemplate.convertAndSend("/sub/chattingroom/" + chattingRoomId,
        chattingMessageService.sendChattingMessage(
            (User) Objects.requireNonNull(accessor.getSessionAttributes()).get("user"),
            sendChattingMessageForm));
  }

  @GetMapping("/v1/chattingroom/{chattingRoomId}")
  public ResponseEntity<Page<ChattingMessageDto>> getAllChattingMessage(
      @PathVariable Long chattingRoomId, @AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(chattingMessageService.getAllChattingMessage(
        user, chattingRoomId, pageable));
  }


}
