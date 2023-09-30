package com.gnakkeoyhgnus.noteforios.domain.repository;

import com.gnakkeoyhgnus.noteforios.domain.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {

}
