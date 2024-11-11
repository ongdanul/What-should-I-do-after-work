package com.elice.boardproject.board.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardResponseDto {
    private Long boardId;
    private String boardTitle;
}
