package com.gnakkeoyhgnus.noteforios.domain.repository;

import com.gnakkeoyhgnus.noteforios.domain.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

}
