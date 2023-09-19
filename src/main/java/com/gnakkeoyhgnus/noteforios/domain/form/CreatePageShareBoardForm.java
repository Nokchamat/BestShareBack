package com.gnakkeoyhgnus.noteforios.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreatePageShareBoardForm {

  private String title;

  private String explains;

}
