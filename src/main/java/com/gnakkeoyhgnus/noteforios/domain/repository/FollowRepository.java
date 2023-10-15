package com.gnakkeoyhgnus.noteforios.domain.repository;

import com.gnakkeoyhgnus.noteforios.domain.entity.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
  Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

  Page<Follow> findAllByFollowerId(Long followerId, Pageable pageable);

  List<Follow> findAllByFollowingId(Long id);

  Long countByFollowingId(Long followingId);
}
