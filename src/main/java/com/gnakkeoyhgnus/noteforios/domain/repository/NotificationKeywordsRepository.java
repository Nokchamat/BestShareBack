package com.gnakkeoyhgnus.noteforios.domain.repository;

import com.gnakkeoyhgnus.noteforios.domain.entity.NotificationKeywords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationKeywordsRepository extends JpaRepository<NotificationKeywords, Long> {
  Page<NotificationKeywords> findAllByUserId(Long id, Pageable pageable);
}
