package com.cis.board.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
public class fileVO {

    private int file_id;                   //파일 num  pk
    private String category;              //category 게시판 fk
    private int board_num;           // boardnum id fk
    private String original_name;       //원본 파일명
    private String save_name;           // 저장 파일명
    private int file_size;              //파일 크기
    private String created_date;    //만든날짜

    @Builder
    public fileVO(String original_name, String save_name, int file_size) {
        this.original_name = original_name;
        this.save_name = save_name;
        this.file_size = file_size;
    }


}
