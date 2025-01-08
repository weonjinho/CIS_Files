package com.cis.board.controller;

import com.cis.board.paging.Pagination;
import com.cis.board.paging.PagingResponse;
import com.cis.board.service.IF_board_service;
import com.cis.board.util.FIleDataUtil;
import com.cis.board.vo.boardVO;
import com.cis.board.vo.fileVO;
import com.cis.board.vo.searchDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class Board_Manager_Controller {

    private final IF_board_service ifboardservice;
    private final FIleDataUtil filedatautil;

    //게시판 관리 메인 컨트롤러
    @GetMapping(value = "/board/manager")
    public String board_fr(@ModelAttribute searchDTO params, Model model,
                           HttpSession session) throws Exception {
//        System.out.println("--------------");
//        System.out.println(params.toString() + "   //search Dto확인");
//        System.out.println("--------------");

        boolean admLoginFlag = false;
        String emp_name = "";
        if (session.getAttribute("admin") != null) {
            System.out.println("자유게시판!!");
            System.out.println(session.getAttribute("emp_name") + "  //유저네임");
            System.out.println(session.getAttribute("employee_id") + "   /아이디");
            System.out.println(session.getAttribute("emp_rank") + "  //랭크");

            String emp_id = (String) session.getAttribute("admin");
            emp_name = ifboardservice.getNameById(emp_id);
            session.setAttribute("emp_name", emp_name);

            model.addAttribute("emp_id", emp_id);
            model.addAttribute("emp_name", emp_name);
            admLoginFlag = true;
            //model.addAttribute("rank", rank);
        }
        //
        PagingResponse<boardVO> boardvolist = ifboardservice.findAllPost_adm(params);

        // Null 체크 및 기본값 설정
        if (boardvolist == null || boardvolist.getList() == null) {
            boardvolist = new PagingResponse<>();
            boardvolist.setList(new ArrayList<>()); // 빈 리스트로 초기화
            boardvolist.setPagination(new Pagination(1, params)); // 페이징 정보 기본값
        }

        //emp_id에 emp_name 삽입
        for (boardVO boardvo : boardvolist.getList()) {
            //board테이블의 id를 param으로 보내고 멤버 테이블에서 이름을 return 받는다.
            emp_name = ifboardservice.getNameById(boardvo.getEmp_id());
            System.out.println(ifboardservice.getNameById(boardvo.getEmp_id()));
            //예외 처리
            if (emp_name == null) {
                emp_name = "";
            }
            boardvo.setEmp_id(emp_name);

        }

        for (boardVO boardvo : boardvolist.getList()) {
            System.out.println("관리자글보기: " + boardvo.toString());
        }

        //boardvolist.getPagination().getStartPage()
        //현재 검색 조건과 페이징 정보 추가
        model.addAttribute("boardvolist", boardvolist);
        model.addAttribute("keyword", params.getKeyword());
        model.addAttribute("searchType", params.getSearchType());
        model.addAttribute("searchCategory", params.getSearchCategory());
        model.addAttribute("currentPage", params.getPage());
        model.addAttribute("admLoginFlag", admLoginFlag);

        return "/board/board_mng/board_Admin";
    }

    //관리자 게시판 글 보기/
    @GetMapping(value = "/board/manager/{category}/{board_num}")
    public String fr_preview(@PathVariable("category") String category, @PathVariable("board_num") Integer num,
                             Model model, HttpSession session) throws Exception {

        System.out.println(session.getAttribute("admin") + "   admin ?");
        System.out.println(session.getAttribute("emp_id") + "emp id");
        System.out.println(session.getAttribute("employee_id")+"   /임플로이 아이디");
            //세션 로그인 확인
            boolean loginFlag = false;
            String sessionId = "";
            String loggedNanme = "";
            if (session.getAttribute("employee_id") != null) {
    //            System.out.println("자유게시판 글보기!");
    //            System.out.println(session.getAttribute("emp_name")+"  //유저네임");
    //            System.out.println(session.getAttribute("employee_id")+"   /아이디");
    //            System.out.println(session.getAttribute("emp_rank")+"  //랭크");
                sessionId = (String) session.getAttribute("employee_id");
                loggedNanme = (String) session.getAttribute("emp_name");
                System.out.println("일반회원");
    //            String rank = (String) session.getAttribute("emp_rank");

            } else if (session.getAttribute("admin") != null) {
                sessionId = (String) session.getAttribute("admin");
                loggedNanme = (String) session.getAttribute("emp_name");
                loginFlag = true;
                System.out.println("세션아이디 : " + sessionId + "// " + "이름  :  " + loggedNanme);
            }

            System.out.println(num + "  게시글넘버");
            System.out.println("pathvariable :" + num + "//" + category);
            //조회수
            ifboardservice.readBoard(num);
            //내용 옮기기 (관리자용)
            boardVO boardvo;
            if (category.equals("공지사항")) {
    //            System.out.println("category chekc" + category);
                boardvo = ifboardservice.viewOne(num);
            } else if (category.equals("자유게시판")) {
                //            System.out.println("category chekc" + category);
                boardvo = ifboardservice.viewOne_fr(num);
            }else {
                boardvo = new boardVO();
            }
                String emp_name = ifboardservice.getNameById(boardvo.getEmp_id());
                //게시판 id와 세션 로그인한 아이디 비교
                //        System.out.println(boardvo.getEmp_id()+"       text 게시판id");
                //        System.out.println(sessionId+"     text 세션로그인");
                if (boardvo.getEmp_id().equals(sessionId)) {
                    System.out.println("게시판 id와 로그인한 id 일치");
                    loginFlag = true;

                } else if (sessionId.equals("admin")) {
                    System.out.println("관리자 아이디임을 확인 ");
                    loginFlag = true;
                }

                //파일 테이블에서 사용할 카테고리 파라미터
                String categoryTemp = boardvo.getCategory();

                System.out.println("카테고리 테스트:  " + categoryTemp);
                //file board 가져오기
                List<fileVO> fileList = ifboardservice.getAttach(num, categoryTemp);
                //db에서 가져온값 확인
                for (fileVO file : fileList) {
                    System.out.println(file.toString());
                }

                //model에 전송할 값들 추가
                model.addAttribute("loggedId", sessionId); //로그인한 사용자 아이디
                model.addAttribute("loggedNanme", loggedNanme); //로그인한 사용자이름
                model.addAttribute("loginFlag", loginFlag);
                model.addAttribute("boardvo", boardvo);
                model.addAttribute("fileList", fileList);
                model.addAttribute("emp_name", emp_name);
                model.addAttribute("category", category);
                System.out.println(boardvo.toString() + "boardvo");

                return "/board/board_mng/fr_preview";
    }


        //게시판 글 삭제 (num,category) + 파일삭제
        @PostMapping(value = "/board/manager/delOne/")
        @ResponseBody
        public Map<String, Object> bord_manager_delOne(@RequestParam(value = "category") String category,
                                                        @RequestParam(value = "board_num") Integer num,
                                                        @RequestParam(value = "fileAttached") Integer fileAttached) throws Exception {
//            System.out.println("게시글 번호: " + num);
//            System.out.println("카테고리: " + category);
//            System.out.println("파일첨부여부: " + fileAttached);
            Map<String, Object> response = new HashMap<>();

            try {
                // 파일 삭제 로직
                if (fileAttached != 0) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("category", category);
                    params.put("board_num", num);
                    ifboardservice.delfilesBybdNumCategory(params);
                }

                // 게시글 삭제 로직
                boardVO boardvo = new boardVO();
                boardvo.setBoard_num(num);
                boardvo.setCategory(category);
                ifboardservice.deleteOne(boardvo);

                System.out.println("삭제 완료");

                // 성공 응답 데이터 작성
                response.put("status", "success");
                response.put("redirectUrl", "/board/manager"); // 리다이렉트할 URL
            } catch (Exception e) {
                e.printStackTrace();
                response.put("status", "error");
                response.put("message", "게시글 삭제 중 오류가 발생했습니다.");
            }

            return response; // JSON 응답
        }
        //글 안 수정 삭제
        //공지게시판 수정
        @PostMapping(value = "/board/manager/modOne/")
        public String gj_modifyOne(@ModelAttribute boardVO boardvo,
                                   @RequestParam(value = "deleteFiles", required = false) List<String> deleteFileIds,
                                   @RequestPart(value = "files", required = false) List<MultipartFile> file) throws Exception {

//
//        ifboardservice.modOne(boardvo);
//        //System.out.println(num + "     a asdf");
//        System.out.println(boardvo.toString() + "   boardvo테스트");
            System.out.println(boardvo.toString());
            System.out.println(file+ "files " );
            System.out.println(deleteFileIds+ "deleteFileIds");
            //카테고리를 parameter에 추가
            String categoryTemp = boardvo.getCategory();
            System.out.println(boardvo.getCategory());
            //파일 삭제

            // 파일 삭제 처리
            if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
                System.out.println("del 파일: " + deleteFileIds);
                ifboardservice.fileDel(deleteFileIds, categoryTemp);
            } else {
                System.out.println("노데이터 ");
            }

            //파일 추가
            // 파일이 null이면 빈 리스트로 초기화
            if (file == null) {
                file = new ArrayList<>();
            }

            //파일 경로에 저장 하고 새로운 파일 이름 리턴
            List<fileVO> fileVoList = filedatautil.savefiles(file);
            //boardvo에 첨부된 파일 갯수를 저장
            boardvo.setFileAttached(filedatautil.attaced(file));

            //게시판 업데이트
            ifboardservice.modOne(boardvo);

            //파일 업데이트(추가)
            for (fileVO fileone : fileVoList) {
                fileone.setBoard_num(boardvo.getBoard_num());
                fileone.setCategory(categoryTemp);
            }
            ifboardservice.modfile(fileVoList);



            //System.out.println(num + "     a asdf");
            System.out.println(boardvo.toString() + "  수정작업완료 ");

            return "redirect:/board_gj";
        }

    //게시판 글 삭제 (num,category) + 파일삭제
    @PostMapping(value = "/board/manager/category/delOne/{num}")
    public String gj_preview_delOne(@PathVariable("num") int num,@RequestParam(value = "category", required = true) String category,
                                    @RequestParam(value = "delfileIds",required = false) List<String> deleteFileIds) throws Exception {

        System.out.println("게시글 번호: " + num);
        System.out.println("카테고리: " + category);
        System.out.println("삭제할 파일 IDs: " + deleteFileIds);

        // deleteFileIds null 체크 추가
        if (deleteFileIds == null) {
            deleteFileIds = new ArrayList<>();
        }

        System.out.println("삭제할 파일 IDs: " + deleteFileIds);

        // 파일 삭제 처리
        if (!deleteFileIds.isEmpty()) {
            System.out.println("삭제할 파일 IDs: " + deleteFileIds);
            ifboardservice.fileDel(deleteFileIds, category);
        } else {
            System.out.println("삭제할 파일이 없음");
        }

        // 게시글 삭제
        boardVO boardvo = new boardVO();
        boardvo.setBoard_num(num);
        boardvo.setCategory(category);
        System.out.println(boardvo.toString() + "boardvo");

        String categoryTemp = boardvo.getCategory();
        boardvo.setBoard_num(num);

        ifboardservice.deleteOne(boardvo); // 삭제 처리

        //System.out.println(boardvo.getCategory() + "카테고리");
        //System.out.println(num+"게시글넘버");
        System.out.println("삭제완료 ");

        return "redirect:/board_gj";
    }


    //


     //공지사항 관리
    @GetMapping(value = "/board/manager/gj")
    public String board(@ModelAttribute searchDTO params, Model model,
                        HttpSession session) throws Exception {


//        System.out.println("공지게시판");
//        System.out.println(session.getAttribute("employee_id"));
//        System.out.println(session.getAttribute("emp_name"));
        //세션 로그인 설정. 임시로
        boolean loginFlag = false;
        String emp_id ="";
        String name ="";
        String emp_name = "";
        if (session.getAttribute("admin") != null) {
            System.out.println(session.getAttribute("admin") + "  //관리자 id");
            emp_id = (String) session.getAttribute("admin");
            name = ifboardservice.getNameById(emp_id);
            System.out.println(name + " ///");
//            System.out.println(session.getAttribute("employee_id") + "   /아이디");
//            System.out.println(session.getAttribute("emp_rank") + "  //랭크");
            loginFlag = true;

        } else if (session.getAttribute("employee_id") != null) {
            System.out.println("자유게시판!!");
            System.out.println(session.getAttribute("emp_name") + "  //유저네임");
            System.out.println(session.getAttribute("employee_id") + "   /아이디");
            System.out.println(session.getAttribute("emp_rank") + "  //랭크");

            emp_id = (String) session.getAttribute("employee_id");
            emp_name = (String) session.getAttribute("emp_name");
            String rank = (String) session.getAttribute("emp_rank");

            model.addAttribute("emp_id", emp_id);
            model.addAttribute("emp_name", emp_name);
            model.addAttribute("rank", rank);


        } else {
            System.out.println("로그인 실패");
        }

        PagingResponse<boardVO> boardvolist = ifboardservice.findAllPost(params);

        //List<boardVO> boardvolist = ifboardservice.findAllPost(params);
        //확인요망. 공지게시판에서는 viewone 메서드 써야될듯

//        for (boardVO boardvo : boardvolist.getList()) {
////            boardVO boardvoo= ifboardservice.viewOne_fr(boardvo.getBoard_num());
//            //board테이블의 id를 param으로 보내고 멤버 테이블에서 이름을 return 받는다.
//            System.out.println(boardvo.getEmp_name());
//            boardvo.setEmp_id(name);
//        }
        //확인요망. 공지게시판에서는 viewone 메서드 써야될듯
        for (boardVO boardvo : boardvolist.getList()) {
//            boardVO boardvoo= ifboardservice.viewOne_fr(boardvo.getBoard_num());
            //board테이블의 id를 param으로 보내고 멤버 테이블에서 이름을 return 받는다.
            emp_name = ifboardservice.getNameById(boardvo.getEmp_id());
            System.out.println(ifboardservice.getNameById(boardvo.getEmp_id()));
            //예외 처리
            if (emp_name == null){
                emp_name = "";
            }
            boardvo.setEmp_id(emp_name);

        }


        //boardvolist.getPagination().getStartPage()
        //현재 검색 조건과 페이징 정보 추가
        model.addAttribute("boardvolist", boardvolist);
        model.addAttribute("keyword", params.getKeyword());
        model.addAttribute("searchType", params.getSearchType());

        // 현재 페이지 추가
        model.addAttribute("currentPage", params.getPage());
        model.addAttribute("loginFlag", loginFlag);
        return "/board/board_mng/board_gj";

    }

    //공지사항 글 하나 보기
    @GetMapping(value = "/board/manager/gj_preview/{board_num}")
    public String gj_preview(@PathVariable("board_num") int num, Model model, HttpSession session) throws Exception {

        //세션 로그인 확인
        boolean loginFlag = false;
        String sessionId = "";
        String loggedNanme = "";
        if(session.getAttribute("employee_id") != null) {
//            System.out.println("자유게시판 글보기!");
//            System.out.println(session.getAttribute("emp_name")+"  //유저네임");
//            System.out.println(session.getAttribute("employee_id")+"   /아이디");
//            System.out.println(session.getAttribute("emp_rank")+"  //랭크");
            sessionId = (String) session.getAttribute("employee_id");
            loggedNanme = (String) session.getAttribute("emp_name");
//            String rank = (String) session.getAttribute("emp_rank");

        } else if (session.getAttribute("admin") != null) {
            sessionId = (String) session.getAttribute("admin");
            loggedNanme = (String) session.getAttribute("emp_name");
            System.out.println("관리자아이디 : "+sessionId + "// " + "관리자이름  :  "  + loggedNanme);
            loginFlag = true;
            model.addAttribute("loginFlag", true);
        }



        System.out.println(num + "  게시글넘버");
        ifboardservice.readBoard(num);
        //내용 옮기기
        boardVO boardvo = ifboardservice.viewOne(num);
        //
        //게시판 id를 param으로 이름 return
        String emp_name = ifboardservice.getNameById(boardvo.getEmp_id());
        //게시판 id와 세션 로그인한 아이디 비교
//        System.out.println(boardvo.getEmp_id()+"       text 게시판id");
//        System.out.println(sessionId+"     text 세션로그인");
        if (boardvo.getEmp_id().equals(sessionId)) {
            System.out.println("게시판 id와 로그인한 id 일치");
            loginFlag = true;

        } else if (sessionId.equals("admin")) {
            System.out.println("관리자 아이디임을 확인 ");
            loginFlag = true;
        }

        //파일 테이블에서 사용할 카테고리 파라미터
        String categoryTemp = boardvo.getCategory();

        System.out.println("카테고리 테스트:  " + categoryTemp);
        //file board 가져오기
        List<fileVO> fileList = ifboardservice.getAttach(num, categoryTemp);
        //db에서 가져온값 확인
        for (fileVO file : fileList) {
            System.out.println(file.toString());
        }

        //model에 전송할 값들 추가
        model.addAttribute("loggedId", sessionId); //로그인한 사용자 아이디
        model.addAttribute("loggedNanme", loggedNanme); //로그인한 사용자이름
        model.addAttribute("boardvo", boardvo);
        model.addAttribute("fileList", fileList);
        model.addAttribute("emp_name", emp_name);
        System.out.println(loginFlag+"로긴 플래그 관리자");
        return "/board/board_mng/gj_preview";
    }

    //공지사항 게시판 글쓰기. session 작업중.
    @GetMapping(value = "/board/manager/write_gj")
    public String write_gj(HttpSession session, Model model) throws Exception {

        //세션 로그인 설정. 임시로
        String name = "";
        String emp_id = "";
        if(session.getAttribute("admin") != null) {
//            System.out.println(session.getAttribute("Name")+"  //유저네임");
            emp_id = (String) session.getAttribute("admin");
            System.out.println(session.getAttribute("adm_id")+"   /아이디");
            name = ifboardservice.getNameById(emp_id);
        }else {
            System.out.println("로그인 실패");
        }

        //유저이름을 모델을 통해 뷰로
        model.addAttribute("emp_id", emp_id);
        model.addAttribute("userName", name);

        return "/board/board_mng/write_gj";
    }

    //글쓰기 자유 + 공지
    @PostMapping(value = "/board/manager/write_gj/board_write_one")
    public String board_write_one(@ModelAttribute boardVO boardvo,
                                  @RequestParam(value = "file", required = false) List<MultipartFile> file, HttpServletRequest request) throws Exception {
//

        // 파일이 null이면 빈 리스트로 초기화
        if (file == null) {
            file = new ArrayList<>();
        }
        System.out.println("글쓰기 시작 :" + boardvo.toString());

        //파일 경로에 저장 하고 새로운 파일 이름 리턴

        List<fileVO> fileVoList = filedatautil.savefiles(file);
        //boardvo에 첨부된 파일 갯수를 저장
        boardvo.setFileAttached(filedatautil.attaced(file));

        ifboardservice.writeOneF(boardvo, fileVoList);
        System.out.println("글쓰기 as");
//        //리다이렉트 처리
//        if (boardvo.getCategory().equals("공지사항")) {
//            return "redirect:/board/manager/gj";
//        } else {
//            return "redirect:/board/manager/gj";
//        }

//        // 이전 페이지로 리다이렉트
//        String referer = request.getHeader(HttpHeaders.REFERER);
//        System.out.println("Referer URL: " + referer);
////
//        return "redirect:" + referer; // 이전 페이지가 없을 경우 기본 경로
        return "redirect:/board/manager/gj";

    }


}

