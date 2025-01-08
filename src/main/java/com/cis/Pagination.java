package com.cis;

public class Pagination {
    private int pageSize;          // 1페이지 당 튜플의 수
    private int blockSize = 5;     // 1페이징 당 블럭 버튼의 수
    private int selectPage;        // 현재 선택한 페이지
    private int nowPage = 1;       // 현재 페이지
    private int nowBlock = 1;      // 현재 블럭
    private int totalListCnt;      // 총 튜플 수
    private int totalPageCnt;      // 총 페이지 수
    private int totalBlockCnt;     // 총 블럭 수
    private int startPage = 1;     // 블럭 시작 페이지
    private int endPage = 1;       // 블럭 마지막 페이지
    private int startIndex = 0;    // DB SELECT 인덱스 지정
    private int prevBlock;         // 이전 블럭의 마지막 페이지
    private int nextBlock;         // 다음 블럭의 시작 페이지
    private String filter;

    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getBlockSize() {
        return blockSize;
    }
    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
    public int getSelectPage() {
        return selectPage;
    }
    public void setSelectPage(int selectPage) {
        this.selectPage = selectPage;
    }
    public int getNowPage() {
        return nowPage;
    }
    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }
    public int getNowBlock() {
        return nowBlock;
    }
    public void setNowBlock(int nowBlock) {
        this.nowBlock = nowBlock;
    }
    public int getTotalListCnt() {
        return totalListCnt;
    }
    public void setTotalListCnt(int totalListCnt) {
        this.totalListCnt = totalListCnt;
    }
    public int getTotalPageCnt() {
        return totalPageCnt;
    }
    public void setTotalPageCnt(int totalPageCnt) {
        this.totalPageCnt = totalPageCnt;
    }
    public int getTotalBlockCnt() {
        return totalBlockCnt;
    }
    public void setTotalBlockCnt(int totalBlockCnt) {
        this.totalBlockCnt = totalBlockCnt;
    }
    public int getStartPage() {
        return startPage;
    }
    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }
    public int getEndPage() {
        return endPage;
    }
    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
    public int getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
    public int getPrevBlock() {
        return prevBlock;
    }
    public void setPrevBlock(int prevBlock) {
        this.prevBlock = prevBlock;
    }
    public int getNextBlock() {
        return nextBlock;
    }
    public void setNextBlock(int nextBlock) {
        this.nextBlock = nextBlock;
    }
    public String getFilter() {
        return filter;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "pageSize=" + pageSize +
                ", blockSize=" + blockSize +
                ", selectPage=" + selectPage +
                ", nowPage=" + nowPage +
                ", nowBlock=" + nowBlock +
                ", totalListCnt=" + totalListCnt +
                ", totalPageCnt=" + totalPageCnt +
                ", totalBlockCnt=" + totalBlockCnt +
                ", startPage=" + startPage +
                ", endPage=" + endPage +
                ", startIndex=" + startIndex +
                ", prevBlock=" + prevBlock +
                ", nextBlock=" + nextBlock +
                ", filter=" + filter +
                '}';
    }

    // SELECT로 가져온 튜플의 수와 현재 페이지를 Controller로부터 받아온다.
    // totalListCnt : SELECT로 가져온 튜플의 전체 수
    // nowPage : 현재 페이지
    public Pagination(int pageSize, int totalListCnt, int nowPage) {
        // 1페이지 당 튜플의 수 지정
        setPageSize(pageSize);
        // 현재 페이지, 튜플의 전체 수 저장
        setNowPage(nowPage);
        // 만약 DB에서 조회한 리스트의 수가 없을 경우 1로 고정 (에러 방지)
        if (totalListCnt <= 0) totalListCnt = 1;
        setTotalListCnt(totalListCnt);

        // Math.ceil : 반올림을 위해 Math 클래스의 ceil 메서드 사용
        // totalListCnt와 totalPageCnt의 값은 int이므로 double로 형변환을 위해 (값 * 1.0) 사용
        // 총 페이지 수 : 튜플의 전체 수에 1페이지 당 튜플 수를 나눈 뒤 반올림하여 저장
        // 총 블럭 수 저장 : 총 페이지 수에 1페이지 당 블럭 버튼의 수를 나눈 뒤 반올림하여 저장
        // 현재 블럭 : 현재 페이지에 1페이지 당 블럭 버튼의 수를 나눈 뒤 반올림하여 저장
        setTotalPageCnt((int)Math.ceil((totalListCnt * 1.0) / pageSize));
        setTotalBlockCnt((int)Math.ceil((totalPageCnt * 1.0) / blockSize));
        setNowBlock((int)Math.ceil((nowPage * 1.0) / blockSize));

        // 블럭의 시작과 마지막 페이지 설정
        setStartPage(((nowBlock - 1) * blockSize) + 1);
        setEndPage(startPage + blockSize - 1);
        // 블럭의 마지막 페이지 확인
        if (endPage > totalPageCnt) { this.endPage = totalPageCnt; }

        // 이전 블럭 클릭 시, 이전 블럭의 마지막 페이지 지정
        // 다음 블럭 클릭 시, 다음 블럭의 시작 페이지 지정
        // 조건문(if)을 통해 현재 페이지가 시작/마지막 페이지를 넘어가지 않도록 설정
        setPrevBlock((nowBlock * blockSize) - blockSize);
        if (prevBlock < 1) { this.prevBlock = 1; }
        setNextBlock((nowBlock * blockSize) + 1);
        if (nextBlock > totalPageCnt) { nextBlock = totalPageCnt; }

        // DB SELECT 인덱스 지정
        setStartIndex((nowPage - 1) * pageSize);
    }

}
