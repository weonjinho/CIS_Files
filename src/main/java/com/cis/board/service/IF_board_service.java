package com.cis.board.service;



import com.cis.Pagination;
import com.cis.board.paging.PagingResponse;
import com.cis.board.vo.boardVO;
import com.cis.board.vo.commentVO;
import com.cis.board.vo.fileVO;
import com.cis.board.vo.searchDTO;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Map;

public interface IF_board_service {


    //게시글 작성
//   public void writeOne(boardVO boardvo)throws Exception;
    //게시글에 작성된 id를 보내고 name을 리턴받음
    public String getNameById(String emp_id) throws Exception;
    //게시글작성 // 파일첨부 포함
    public void writeOneF(boardVO boardvo, List<fileVO> fileList)throws Exception;
    //
    public List<boardVO> viewAll()throws Exception;
    //조회수
    public void readBoard(int num)throws Exception;
    //공지사항 게시글보기
    public boardVO viewOne(int num)throws Exception;

    //자유 게시판게시글보기
    public boardVO viewOne_fr(int num)throws Exception;
    //자유 게시판 첨부파일 가져오기
    public List<fileVO> getAttach(int num, String category)throws Exception;

    //선택 게시글 삭제
    public void delOne(int number)throws Exception;
    //선택 게시글삭제 new
    public void deleteOne(boardVO boardvo)throws Exception;
    //선택 게시글 선택 삭제
    public void modOne(boardVO boardvo)throws Exception;
    //자유게시판 게시글 수정시 파일삭제
    public void fileDel(List<String> delids, String categoryTemp)throws Exception;
    //관리자 게시판에서 글삭제시 파일삭제
//    ifboardservice.delfilesBybdNumCategory(params);
    public void delfilesBybdNumCategory(Map<String,Object> params) throws Exception;
    // 공지사항 게시글 리스트 조회 // param - searchDTO // return- 게시글 리스트(boardVO)
    public PagingResponse<boardVO> findAllPost(searchDTO params)throws Exception;

    // 자유게시판 게시글 리스트 조회 // param - searchDTO // return- 게시글 리스트(boardVO)
    public PagingResponse<boardVO> findAllPost_fr(searchDTO params)throws Exception;
    //모든 게시글 보기
    public PagingResponse<boardVO> findAllPost_adm(searchDTO params)throws Exception;

    //자유 게시판 파일 수정(추가)
    public void modfile(List<fileVO> filevoList)throws Exception;

    //댓글 글쓰기;
    public boolean addCommentOne(commentVO commentvo)throws Exception;
    //댓글 가져오기;
    //List<commentVO> returunList = ifboardservice.(params);
    public List<commentVO> viewComment(Map<String, Object> params )throws Exception;

    boolean deleteCommentByCategoryAndId(Map<String, Object> params) throws Exception;
    //댓글 삭제시 정보 가져오기
    commentVO getCmtByparamsOne(Map<String, Object> params) throws Exception;

}
