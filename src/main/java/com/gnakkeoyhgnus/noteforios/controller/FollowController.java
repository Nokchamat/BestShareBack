package com.gnakkeoyhgnus.noteforios.controller;

import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.service.FollowService;
import lombok.RequiredArgsConstructor;
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
public class FollowController {

  private final FollowService followService;

  @PostMapping("/user/{followingUserId}/follow")
  public ResponseEntity<Void> addFollow(@PathVariable Long followingUserId,
      @AuthenticationPrincipal User user) {

    followService.addFollow(followingUserId, user);

    return ResponseEntity.ok(null);
  }

  @GetMapping("/follow")
  public ResponseEntity<?> getAllMyFollow(@AuthenticationPrincipal User user,
      Pageable pageable) {

    return ResponseEntity.ok(followService.getAllMyFollow(user, pageable));
  }

  @DeleteMapping("/follow/{followId}")
  public ResponseEntity<Void> deleteFollow(@PathVariable Long followId,
      @AuthenticationPrincipal User user) {

    followService.deleteFollow(followId, user);

    return ResponseEntity.ok(null);
  }

}
