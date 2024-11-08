package com.elice.boardproject.board.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardDto {
    private Long boardId;
    private String boardTitle;

    public BoardResponseDto toBoardResponseDto() {
        return BoardResponseDto.builder()
                .boardTitle(boardTitle)
                .build();
    }
}
