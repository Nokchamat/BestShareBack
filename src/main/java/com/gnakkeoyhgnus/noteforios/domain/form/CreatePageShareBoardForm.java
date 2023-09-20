package com.gnakkeoyhgnus.noteforios.domain.form;

import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.WRITE_THE_EXPLAIN;
import static com.gnakkeoyhgnus.noteforios.exception.ErrorCode.WRITE_THE_TITLE;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreatePageShareBoardForm {

  @NotEmpty(message = WRITE_THE_TITLE)
  private String title;

  @NotEmpty(message = WRITE_THE_EXPLAIN)
  private String explains;

}
