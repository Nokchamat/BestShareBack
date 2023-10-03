package com.gnakkeoyhgnus.noteforios.domain.repository;

import com.gnakkeoyhgnus.noteforios.domain.entity.ChattingRoom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  Optional<ChattingRoom> findByFirstUserIdAndSecondUserId(Long firstUserId, Long secondUserId);

  Page<ChattingRoom> findAllByFirstUserIdAndFirstUserExistIsTrueOrSecondUserIdAndSecondUserExistIsTrue(Long firstUserId, Long secondUserId, Pageable pageable);
}
