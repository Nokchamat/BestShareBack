package com.gnakkeoyhgnus.noteforios.controller;

import com.gnakkeoyhgnus.noteforios.domain.dto.LikesDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.service.LikesService;
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
public class LikesController {

  private final LikesService likesService;

  @PostMapping("/pageshareboard/{pageShareBoardId}/likes")
  ResponseEntity<Void> addLikes(@AuthenticationPrincipal User user,
      @PathVariable Long pageShareBoardId) {

    likesService.addLikes(user, pageShareBoardId);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/likes")
  ResponseEntity<Page<LikesDto>> getMyLikes(
      @AuthenticationPrincipal User user, Pageable pageable) {

    return ResponseEntity.ok(likesService.getMyLikes(user, pageable));
  }

  @DeleteMapping("/likes/{likesId}")
  ResponseEntity<Void> deleteLikes(@AuthenticationPrincipal User user,
      @PathVariable Long likesId) {

    likesService.deleteLikes(user, likesId);

    return ResponseEntity.ok(null);
  }

}
