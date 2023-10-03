package com.gnakkeoyhgnus.noteforios.controller;

import com.gnakkeoyhgnus.noteforios.domain.dto.ChattingRoomDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ChattingRoomController {

  private final ChattingRoomService chattingRoomService;

  @PostMapping("/user/{userId}/chattingroom")
  public ResponseEntity<Void> createChattingRoom(@AuthenticationPrincipal User user,
      @PathVariable Long userId) {

    chattingRoomService.createChattingRoom(user, userId);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/chattingroom")
  public ResponseEntity<Page<ChattingRoomDto>> getChattingRoom(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(chattingRoomService.getChattingRoom(user, pageable));
  }

  @DeleteMapping("/chattingroom/{chattingroomId}")
  public ResponseEntity<Void> leaveChattingRoom(@AuthenticationPrincipal User user,
      @PathVariable Long chattingroomId) {

    chattingRoomService.leaveChattingRoom(user, chattingroomId);

    return ResponseEntity.ok(null);
  }

}
