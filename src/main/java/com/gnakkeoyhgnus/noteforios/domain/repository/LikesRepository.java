package com.gnakkeoyhgnus.noteforios.domain.repository;

import com.gnakkeoyhgnus.noteforios.domain.entity.Likes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

  Optional<Likes> findByUserIdAndPageShareBoardId(Long id, Long pageShareBoardId);

  Long countByPageShareBoardId(Long pageShareBoardId);

  Page<Likes> findByUserId(Long id, Pageable pageable);

  void deleteAllByPageShareBoardId(Long pageShareBoardId);

  @Query("SELECT l.pageShareBoard.id, COUNT(l.pageShareBoard.id) as counted_id " +
      "FROM Likes l GROUP BY l.pageShareBoard.id order by counted_id desc")
  List<Long[]> findAllByCountPageShareBoardId(Pageable pageable);
}
