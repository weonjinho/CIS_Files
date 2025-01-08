package com.cis.board.vo;

import com.cis.board.paging.Pagination;
import lombok.Data;

@Data
public class searchDTO {

    private int page;           // 현재 페이지 번호
    private int recordSize;     // 페이지당 출력할 데이터 개수(예:10)
    private int pageSize;       // 화면 하단에 출력할 페이지 사이즈
    private String keyword;     // 검색 키워드
    private String searchType;      // 검색 유형
    private String searchCategory;  // 검색 카테고리
    private Pagination pagination; // 페이지네이션 정보

    //생성자
    public searchDTO() {
        this.page = 1; // 페이지1
        this.recordSize = 10; // 페이지당 출력할 데이터개수 : 10
        this.pageSize = 10;  // 하단 출력할 페이즈 사이즈 : 10
    }

    public int getOffset() {
        return (page - 1) * recordSize;
    }


}
