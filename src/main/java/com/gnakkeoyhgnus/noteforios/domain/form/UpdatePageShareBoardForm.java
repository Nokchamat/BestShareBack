package com.gnakkeoyhgnus.noteforios.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePageShareBoardForm {

  private String title;

  private String explains;

}
