package com.gnakkeoyhgnus.noteforios.service;

import com.gnakkeoyhgnus.noteforios.domain.dto.LikesDto;
import com.gnakkeoyhgnus.noteforios.domain.entity.Likes;
import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import com.gnakkeoyhgnus.noteforios.domain.entity.User;
import com.gnakkeoyhgnus.noteforios.domain.repository.LikesRepository;
import com.gnakkeoyhgnus.noteforios.domain.repository.PageShareBoardRepository;
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
public class LikesService {

  private final LikesRepository likesRepository;

  private final PageShareBoardRepository pageShareBoardRepository;

  @Transactional
  public void addLikes(User user, Long pageShareBoardId) {
    PageShareBoard pageShareBoard = pageShareBoardRepository.findById(pageShareBoardId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PAGE_SHARE_BOARD));

    likesRepository.findByUserIdAndPageShareBoardId(user.getId(), pageShareBoardId)
        .ifPresent(likes -> {
          throw new CustomException(ErrorCode.ALREADY_ADD_LIKES);
        });

    likesRepository.save(Likes.builder()
        .user(user)
        .pageShareBoard(pageShareBoard)
        .build());
  }


  public Page<LikesDto> getMyLikes(User user, Pageable pageable) {
    return likesRepository.findByUserId(user.getId(), pageable)
        .map(LikesDto::fromEntity);
  }

  @Transactional
  public void deleteLikes(User user, Long likesId) {
    Likes likes = likesRepository.findById(likesId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LIKES));

    if (!Objects.equals(user.getId(), likes.getUser().getId())) {
      throw new CustomException(ErrorCode.PERMISSION_DENIED_TO_DELETE);
    }

    likesRepository.delete(likes);
  }
}
