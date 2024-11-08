package com.elice.boardproject.board.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Board {
    private Long boardId;
    private String boardTitle;
}
