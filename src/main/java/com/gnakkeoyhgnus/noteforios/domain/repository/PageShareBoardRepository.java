package com.gnakkeoyhgnus.noteforios.domain.repository;

import com.gnakkeoyhgnus.noteforios.domain.entity.PageShareBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageShareBoardRepository extends JpaRepository<PageShareBoard, Long> {

  Page<PageShareBoard> findAllByUserId(Long userId, Pageable pageable);

}
