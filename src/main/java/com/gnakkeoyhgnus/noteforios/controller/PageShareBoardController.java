package com.gnakkeoyhgnus.noteforios.controller;

import com.gnakkeoyhgnus.noteforios.domain.dto.PageSharedBoardDto;
import com.gnakkeoyhgnus.noteforios.domain.dto.PageSharedBoardListDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.CreatePageShareBoardForm;
import com.gnakkeoyhgnus.noteforios.domain.form.UpdatePageShareBoardForm;
import com.gnakkeoyhgnus.noteforios.service.PageShareBoardService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pageshareboard")
public class PageShareBoardController {

  private final PageShareBoardService pageShareBoardService;

  @PostMapping
  public ResponseEntity<String> createPageShareBoard(
      @RequestPart @Valid CreatePageShareBoardForm createPageShareBoardForm,
      @RequestPart MultipartFile thumbnail,
      @RequestPart MultipartFile pagePDF,
      @AuthenticationPrincipal User user) {

    pageShareBoardService.createPageShareBoard(
        user, createPageShareBoardForm, thumbnail, pagePDF);

    return ResponseEntity.ok("게시글이 작성됐습니다.");
  }

  @GetMapping
  public ResponseEntity<Page<PageSharedBoardListDto>> getAllPageShareBoard(Pageable pageable) {
    return ResponseEntity.ok(pageShareBoardService.getAll(pageable));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<Page<PageSharedBoardListDto>> getAllPageShareBoardByUserId(
      @PathVariable Long userId,  Pageable pageable) {
    return ResponseEntity.ok(pageShareBoardService.getAllByUserId(userId, pageable));
  }

  @GetMapping("/{pageShardBoardId}")
  public ResponseEntity<PageSharedBoardDto> getDetailForPageShareBoardId(
      @AuthenticationPrincipal User user,
      @PathVariable Long pageShardBoardId) {

    return ResponseEntity.ok(
        pageShareBoardService.getPageShareBoardDetailForPageShareBoardId(user, pageShardBoardId));
  }

  @GetMapping("/best")
  public ResponseEntity<List<PageSharedBoardListDto>> getBestPageShareBoardList() {

    return ResponseEntity.ok(pageShareBoardService.getBestPageShareBoardList());
  }

  @DeleteMapping("/{pageShareBoardId}")
  public ResponseEntity<String> deletePageShareBoard(@AuthenticationPrincipal User user,
      @PathVariable Long pageShareBoardId) {

    pageShareBoardService.deletePageShareBoard(user, pageShareBoardId);

    return ResponseEntity.ok("게시글이 삭제됐습니다.");
  }

  @PutMapping("/{pageShareBoardId}")
  public ResponseEntity<String> updatePageShareBoard(@AuthenticationPrincipal User user,
      @PathVariable Long pageShareBoardId,
      @RequestPart UpdatePageShareBoardForm updatePageShareBoardForm,
      @RequestPart MultipartFile thumbnail,
      @RequestPart MultipartFile pagePDF) {

    pageShareBoardService.updatePageShareBoard(
        user, pageShareBoardId, updatePageShareBoardForm, thumbnail, pagePDF);

    return ResponseEntity.ok("게시글이 수정됐습니다.");
  }


}
