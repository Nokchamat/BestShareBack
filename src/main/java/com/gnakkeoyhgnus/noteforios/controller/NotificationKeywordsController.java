package com.gnakkeoyhgnus.noteforios.controller;

import com.gnakkeoyhgnus.noteforios.domain.dto.NotificationKeywordsDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.service.NotificationKeywordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notificationkeywords")
public class NotificationKeywordsController {

  private final NotificationKeywordsService notificationKeywordsService;

  @DeleteMapping("/{notificationKeywordsId}")
  public ResponseEntity<Void> deleteNotificationKeywords(@PathVariable Long notificationKeywordsId,
      @AuthenticationPrincipal User user) {

    notificationKeywordsService.deleteNotificationKeywords(user, notificationKeywordsId);

    return ResponseEntity.ok(null);
  }

  @GetMapping
  public ResponseEntity<Page<NotificationKeywordsDto>> getNotificationKeywords(
      @AuthenticationPrincipal User user, Pageable pageable) {

    return ResponseEntity.ok(
        notificationKeywordsService.getAllNotificationKeywords(user, pageable));
  }

}
