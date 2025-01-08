package com.cis.board.vo;

import lombok.Data;

@Data
public class boardVO {

   private String category;
   private int board_num;
   private String board_title;
   private String board_content;
   private String create_at;
   private String emp_id;
   private int boardHits;
   private int fileAttached;
   private String emp_name;


}
