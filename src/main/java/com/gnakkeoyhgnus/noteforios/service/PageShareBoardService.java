package com.gnakkeoyhgnus.noteforios.service;

import static java.lang.String.format;

import com.gnakkeoyhgnus.noteforios.domain.dto.PageSharedBoardDto;
import com.gnakkeoyhgnus.noteforios.domain.dto.PageSharedBoardListDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.form.CreatePageShareBoardForm;
import com.gnakkeoyhgnus.noteforios.domain.form.UpdatePageShareBoardForm;
import com.gnakkeoyhgnus.noteforios.domain.repository.FollowRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.LikesRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.PageShareBoardRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.UserRepository;
import com.gnakkeoyhgnus.noteforios.exception.CustomException;
import com.gnakkeoyhgnus.noteforios.exception.ErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PageShareBoardService {

  private final PageShareBoardRepository pageShareBoardRepository;

  private final UserRepository userRepository;

  private final LikesRepository likesRepository;

  private final FollowRepository followRepository;

  private final AmazonS3Service amazonS3Service;

  private final NotificationKeywordsService notificationKeywordsService;

  @Transactional
  public void createPageShareBoard(User user,
      CreatePageShareBoardForm createPageShareBoardForm, MultipartFile thumbnail,
      MultipartFile pagePdfFile) {

    PageShareBoard pageShareBoard = pageShareBoardRepository.save(PageShareBoard.builder()
        .title(createPageShareBoardForm.getTitle())
        .explains(createPageShareBoardForm.getExplains())
        .user(user)
        .viewCount(0L)
        .build());

    pageShareBoard.setFilesUrl(
        amazonS3Service.uploadForThumbnail(thumbnail, pageShareBoard.getId()),
        amazonS3Service.uploadForPDF(pagePdfFile, pageShareBoard.getId())
    );

    String keywords = format("%s님의 게시물이 올라왔습니다.", user.getNickname());
    followRepository.findAllByFollowingId(user.getId()).forEach(follow ->
        notificationKeywordsService.addNotificationKeywords(follow.getFollower(), keywords)
    );
  }

  public Page<PageSharedBoardListDto> getAll(Pageable pageable) {
    return pageShareBoardRepository.findAll(pageable)
        .map(pageShareBoard -> PageSharedBoardListDto.builder()
            .id(pageShareBoard.getId())
            .title(pageShareBoard.getTitle())
            .thumbnailUrl(pageShareBoard.getThumbnailUrl())
            .viewCount(pageShareBoard.getViewCount())
            .likesCount(
                likesRepository.countByPageShareBoardId(pageShareBoard.getId()))
            .build());
  }

  @Transactional
  public PageSharedBoardDto getPageShareBoardDetailForPageShareBoardId(Long pageShardBoardId) {

    PageShareBoard pageShareBoard = pageShareBoardRepository.findById(pageShardBoardId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PAGE_SHARE_BOARD));

    pageShareBoard.increaseViewCount();

    PageSharedBoardDto pageSharedBoardDto = PageSharedBoardDto.fromEntity(pageShareBoard);

    userRepository.findById(pageShareBoard.getUser().getId())
        .ifPresent(user -> pageSharedBoardDto.setUserNickname(user.getNickname()));

    pageSharedBoardDto.setLikesCount(likesRepository.countByPageShareBoardId(pageShardBoardId));

    return pageSharedBoardDto;
  }

  @Transactional
  public void deletePageShareBoard(User user, Long pageShareBoardId) {

    PageShareBoard pageShareBoard = pageShareBoardRepository.findById(pageShareBoardId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PAGE_SHARE_BOARD));

    if (!Objects.equals(user.getId(), pageShareBoard.getUser().getId())) {
      throw new CustomException(ErrorCode.PERMISSION_DENIED_TO_DELETE);
    }

    amazonS3Service.deleteAllFileForPageShareBoard(pageShareBoard);

    likesRepository.deleteAllByPageShareBoardId(pageShareBoard.getId());

    pageShareBoardRepository.delete(pageShareBoard);
  }

  @Transactional
  public void updatePageShareBoard(
      User user, Long pageShareBoardId, UpdatePageShareBoardForm updatePageShareBoardForm,
      MultipartFile thumbnail, MultipartFile pagePDF) {

    PageShareBoard pageShareBoard = pageShareBoardRepository.findById(pageShareBoardId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PAGE_SHARE_BOARD));

    if (!Objects.equals(user.getId(), pageShareBoard.getUser().getId())) {
      throw new CustomException(ErrorCode.PERMISSION_DENIED_TO_UPDATE);
    }

    pageShareBoard.updatePageShareBoard(updatePageShareBoardForm);
    if (!thumbnail.isEmpty()) {
      amazonS3Service.uploadForThumbnail(thumbnail, pageShareBoardId);
    }
    if (!pagePDF.isEmpty()) {
      amazonS3Service.uploadForPDF(pagePDF, pageShareBoardId);
    }
  }
}
