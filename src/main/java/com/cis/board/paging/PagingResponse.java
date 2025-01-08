package com.cis.board.paging;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PagingResponse<T> {
    //자바 제네릭 : T는 type를 의미하며, 어떤 타입의 객체던 데이터로 받겟다는걸 의미함.

    private List<T> list = new ArrayList<>();
    private Pagination pagination;

    public PagingResponse(List<T> list, Pagination pagination) {
        this.list.addAll(list);
        this.pagination = pagination;

    }


    public PagingResponse() {

    }
}
