package com.gnakkeoyhgnus.noteforios.domain.entity;

import com.gnakkeoyhgnus.noteforios.domain.form.UpdatePageShareBoardForm;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PageShareBoard extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String explains;

  private String thumbnailUrl;

  private String pagePDFUrl;

  private Long viewCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public void increaseViewCount() {
    viewCount++;
  }

  public void setFilesUrl(String thumbnailUrl, String pagePDFUrl) {
    this.thumbnailUrl = thumbnailUrl;
    this.pagePDFUrl = pagePDFUrl;
  }

  public void updatePageShareBoard(UpdatePageShareBoardForm updatePageShareBoardForm) {
    this.title = updatePageShareBoardForm.getTitle();
    this.explains = updatePageShareBoardForm.getExplains();
  }

}
