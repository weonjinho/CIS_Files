package com.cis.board.vo;

import lombok.Data;

@Data
public class commentVO {

    private String category;
    private int board_num;
    private int comment_num;
    private String comment_content;
    private String create_at;
    private String emp_id;
    private String emp_name;

    private boolean isOwner;

}
