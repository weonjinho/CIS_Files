package com.cis.member.dto;

public class PageDTO {
    private int startNo;
    private int endNo;
    private int perPagenum = 10;
    private Integer page;
    private int totalCount;
    private int endPage;
    private int startPage;
    private boolean prev;
    private boolean next;

    private String searchType;
    private String searchKeyword;


    public int getStartNo() {
        return startNo;
    }

    public void setStartNo(int startNo) {
        this.startNo = startNo;
    }

    public int getEndNo() {
        return endNo;
    }

    public void setEndNo(int endNo) {
        this.endNo = endNo;
    }

    public int getPerPagenum() {
        return perPagenum;
    }

    public void setPerPagenum(int perPagenum) {
        this.perPagenum = perPagenum;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public boolean isPrev() {
        return prev;
    }

    public void setPrev(boolean prev) {
        this.prev = prev;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    private void calcPage(){
        startNo = (this.page-1) * perPagenum + 1;
        int tempEnd = (int) (Math.ceil(page/(double) this.perPagenum) * this.perPagenum);
        this.startPage = (tempEnd - this.perPagenum) + 1;
        if(tempEnd * this.perPagenum > this.totalCount){
            this.endPage = (int) Math.ceil(this.totalCount / (double) this.perPagenum);
            if(endPage != page){
                this.endNo = startNo + this.perPagenum - 1;
            }else{
                this.endNo = startNo + this.totalCount % 10 - 1;
            }
        }else{
            this.endPage = tempEnd;
            this.endNo = startNo + this.perPagenum - 1;
        }
        this.prev = this.startPage != 1;
        this.next = this.endPage * this.perPagenum < this.totalCount;
    }


}
