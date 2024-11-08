package com.elice.boardproject.board.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardRequestDto {
    private String boardTitle;

    public BoardDto toBoardDto() {
        return BoardDto.builder()
                .boardTitle(boardTitle)
                .build();
    }
}
