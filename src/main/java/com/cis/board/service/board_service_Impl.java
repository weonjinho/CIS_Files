package com.cis.board.service;


import com.cis.board.paging.Pagination;
import com.cis.board.paging.PagingResponse;
import com.cis.board.repository.IF_Reopository;
import com.cis.board.util.FIleDataUtil;
import com.cis.board.vo.boardVO;
import com.cis.board.vo.commentVO;
import com.cis.board.vo.fileVO;
import com.cis.board.vo.searchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class board_service_Impl implements IF_board_service {


    private final IF_Reopository ifrepository;
    private final FIleDataUtil fileDataUtil;

    //게시글 작성, 공지사항글 자유게시판글 분리 //+file
//    @Override
//    public void writeOne(boardVO boardvo) throws Exception {
//
//                if(boardvo.getCategory().equals("공지사항")) {
//                    ifrepository.insertOne(boardvo);
//                }else {
//                    ifrepository.insertOne_fr(boardvo);
//                }
//
//
//    }

    @Override
    public String getNameById(String emp_id) throws Exception {


        return ifrepository.getNameById(emp_id);
    }

    @Override
    public void writeOneF(boardVO boardvo, List<fileVO> fileList) throws Exception {

        System.out.println(boardvo.getCategory()+ "서비스 카테고리 확인");
        if(boardvo.getCategory().equals("공지사항")) {
            ifrepository.insertOne(boardvo);

        }else {
            ifrepository.insertOne_fr(boardvo);
        }

        //table에 insert된 board의 id가져오기
        String categoryTemp = boardvo.getCategory();
        int board_num_temp = ifrepository.getBoardNum(categoryTemp);
        System.out.println("보드넘 가져온 값 확인:  "+ board_num_temp);

        //file첨부, 게시판 id와 게시판 카테고리 추가하여
        if (!fileList.isEmpty() && fileList != null && boardvo.getFileAttached() != 0) {
            for (fileVO file : fileList) {
                file.setBoard_num(board_num_temp);// 생성된 board_num 설정
                file.setCategory(boardvo.getCategory()); // 카테고리 설정
                ifrepository.insertFile(file);
//                }
            }
        }



    }

    //공지 all
    @Override
    public List<boardVO> viewAll() throws Exception {


        List<boardVO> lista = ifrepository.listAll();
        System.out.println(lista + "서비스단 리스트 확인");
        return lista;
    }
    //자유게시판 all

    //하나보기
    @Override
    public void readBoard(int num) throws Exception {

        ifrepository.updateHits(num);

    }
    //공지사항 선택 게시글 보기
    @Override
    public boardVO viewOne(int num) throws Exception {
        //num으로 가져오니 에러

        return ifrepository.selectOne(num);
    }

    //자유게시판
    @Override
    public boardVO viewOne_fr(int num) throws Exception {

        return ifrepository.selectOne_fr(num);
    }

    @Override
    public List<fileVO> getAttach(int num,String category) throws Exception {
        //Map에 파일 파라미터 설정
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("num", num);


        return ifrepository.selectFile(params);
    }

    //삭제
    @Override
    public void delOne(int number) throws Exception {

        ifrepository.deleteOne(number);
    }

    @Override
    public void deleteOne(boardVO boardvo) throws Exception {

        ifrepository.deleteOnE(boardvo);
    }


    //수정
    @Override
    public void modOne(boardVO boardvo) throws Exception {

        ifrepository.updateOne(boardvo);
    }
    //자유게시판 수정시 파일 삭제할때
    @Override
    public void fileDel(List<String> delIds, String categoryTemp) throws Exception {
        System.out.println(categoryTemp+"////1차 카테고리템프 확인");
        for(String delId : delIds) {

            int delInt = Integer.parseInt(delId); // 변환 확인
            System.out.println(delInt + "delint");

            Map<String, Object> params = new HashMap<>();
            params.put("category", categoryTemp);
            params.put("num", delInt);
            System.out.println(categoryTemp+"////카테고리템프 확인");

            fileVO file = ifrepository.selectFileById(params); // DB 조회 확인
            System.out.println("db조회");
            if (file == null) {
                System.out.println("file이 null입니다. params 확인: " + params);
            } else {
                System.out.println("file 정보: " + file.toString());
            }



            if (file != null) {
                // 로컬 파일 삭제
                System.out.println("파일삭제전");
                boolean isDeleted = fileDataUtil.deleteFile(file.getSave_name());
                System.out.println(file.getSave_name()+ "// savename가져오나 ");
                if (isDeleted) {
                    System.out.println("로컬 파일 삭제 성공: " + file.getSave_name());
                } else {
                    System.out.println("로컬 파일 삭제 실패: " + file.getSave_name());
                }
            }else {
                System.out.println("file 정보: " + file.toString());
            }

            ifrepository.deleteFile(params);
        }

    }

    @Override
    public void delfilesBybdNumCategory(Map<String, Object> params) throws Exception {

        List<fileVO> file = ifrepository.selectFileBybdNumCategory(params);
        for (fileVO fileList: file) {
            System.out.println(fileList.getSave_name());
            System.out.println("파일삭제전");
            boolean isDeleted = fileDataUtil.deleteFile(fileList.getSave_name());
            if (isDeleted) {
                System.out.println("파일삭제 완료: "+ fileList.getSave_name());
            }else {
                System.out.println("파일삭제 실패: "+ fileList.getSave_name());
            }
        }


    }

    //공지사항 게시글 리스트 조회
    // param - searchDTO
    // return - 게시글 리스트(List<boardVO>)
    @Override
    public PagingResponse<boardVO> findAllPost(searchDTO params) throws Exception {
        //조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와
        //null을 담아 반환
        int count = ifrepository.count(params);
        if(count<1) {
            //데이터가 없는 경우에 pagination 객체 생성
            Pagination emtypagination = new Pagination(0,params);// totalrecordcount = 0;
            params.setPagination(emtypagination);

            //빈 pagination 객체 생성
            return new PagingResponse<>(Collections.emptyList(), emtypagination);

        }

        //데이터가 있는 경우 Pagination 생성
        // Paginaton 객체를 생성해서 페이지 정보 계산 후 searchDTO 타입의 객체인
        // params에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        //조회
        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로
        // 리스트 데이터 조회 후 응답 데이터 반환
        List<boardVO> list = ifrepository.fiandAll(params);
        return new PagingResponse<>(list, pagination);

    }

    //자유게시판 게시글 리스트 조회
    // param - searchDTO
    // return - 게시글 리스트(List<boardVO>)
    @Override
    public PagingResponse<boardVO> findAllPost_fr(searchDTO params) throws Exception {
        //조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와
        //null을 담아 반환
        int count = ifrepository.count_fr(params);
        if(count<1) {
            //데이터가 없는 경우에 pagination 객체 생성
            Pagination emtypagination = new Pagination(0,params);// totalrecordcount = 0;
            params.setPagination(emtypagination);

            //빈 pagination 객체 생성
            return new PagingResponse<>(Collections.emptyList(), emtypagination);

        }

        //데이터가 있는 경우 Pagination 생성
        // Paginaton 객체를 생성해서 페이지 정보 계산 후 searchDTO 타입의 객체인
        // params에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        //조회
        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로
        // 리스트 데이터 조회 후 응답 데이터 반환
        List<boardVO> list = ifrepository.fiandAll_fr(params);
        if (list == null) {
            list = new ArrayList<>();
        }

        return new PagingResponse<>(list, pagination);


    }
    //    <관리자 - 모든게시판 글보기>
    @Override
    public PagingResponse<boardVO> findAllPost_adm(searchDTO params) throws Exception {
        //조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와
        //null을 담아 반환
        int count = ifrepository.count_allAdm(params);
        if(count<1) {
            //데이터가 없는 경우에 pagination 객체 생성
            Pagination emtypagination = new Pagination(0,params);// totalrecordcount = 0;
            params.setPagination(emtypagination);

            //빈 pagination 객체 생성
            return new PagingResponse<>(Collections.emptyList(), emtypagination);

        }
        //데이터가 있는 경우 Pagination 생성
        // Paginaton 객체를 생성해서 페이지 정보 계산 후 searchDTO 타입의 객체인
        // params에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        //조회
        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로
        // 리스트 데이터 조회 후 응답 데이터 반환
        List<boardVO> list = ifrepository.findAll_adm(params);
//        for (boardVO boardVO : list) {
//            System.out.println("//"  + boardVO.toString());
//
//
//        }
        if (list == null) {
            list = new ArrayList<>();
        }

        return new PagingResponse<>(list, pagination);

    }

    @Override
    public void modfile(List<fileVO> filevoList) throws Exception {
        //file첨부, 게시판 id와 게시판 카테고리 추가하여
        if (!filevoList.isEmpty() && filevoList != null) {
            for (fileVO file : filevoList) {
                ifrepository.insertFile(file);
//                }
            }
        }

    }

    @Override
    public boolean addCommentOne(commentVO commentvo) throws Exception {

        return ifrepository.insertCommentOne(commentvo);
    }

    @Override
    public List<commentVO> viewComment(Map<String, Object> params) throws Exception {


        if(params != null && !params.isEmpty()) {
            return ifrepository.selectAllCmt(params);
        }else {

            return null;
        }


    }

    @Override
    public boolean deleteCommentByCategoryAndId(Map<String, Object> params) throws Exception {


        return ifrepository.deleteComment(params);
    }

    @Override
    public commentVO getCmtByparamsOne(Map<String, Object> params) throws Exception {

        return ifrepository.getCmtByparamsOne(params);
    }


}
