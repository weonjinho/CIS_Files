package com.cis.board.controller;

import com.cis.board.repository.IF_Reopository;
import com.cis.board.service.IF_board_service;
import com.cis.board.vo.commentVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final IF_board_service ifboardservice;
    //댓글 insert
    @PostMapping(value = "/board/addComment/{num}")
    public ResponseEntity<?> addComment(@PathVariable int num, @RequestBody commentVO commentvO)throws Exception {
//        requestData["category"] = categoryIdbox.value;
//        requestData["board_num"] = num;
        boolean success;
        try {
            success = ifboardservice.addCommentOne(commentvO);
            System.out.println(success+"  :boolean 확인");

        }catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        System.out.println(commentvO.toString());


        // JSON 응답 반환
        return ResponseEntity.ok(Map.of(
                "success", success,
                "message", success ? "댓글이 등록되었습니다." : "댓글 등록에 실패했습니다."
        ));
    }
//    public Map<String, Object> addComment (@ModelAttribute commentVO commentvo)throws Exception {
//
//        System.out.println("comentvo확인 ://");
//        System.out.println(commentvo.toString());
//
//        boolean success = false;
//        try {
//            success = ifboardservice.addCommentOne(commentvo);
//            System.out.println(success+"  :boolean 확인");
//
//        }catch (Exception e) {
//            e.printStackTrace();
//            success = false;
//        }
//
//        //응답
    ////        boolean success = false; // 초기값 설정
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", success);
//        System.out.println(response+ "response확인");
//
//        return response;
//    }

    //댓글 view
    @GetMapping(value = "/board/getComment/{category}/{num}")
    public Map<String, Object> viewComment (@PathVariable String category, @PathVariable Integer num,
                                            HttpSession session)throws Exception {

        boolean success = false;

        System.out.println(category + " // " + num);


        //paramters
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("board_num", num);
        System.out.println(params+ "  :params 확인");

        //return
        List<commentVO> comments = ifboardservice.viewComment(params);

        for (commentVO commentvo : comments) {
            System.out.println("commentvo 확인");
            System.out.println(commentvo.toString());
            String emp_name = ifboardservice.getNameById(commentvo.getEmp_id());
        }

        //  String emp_name = ifboardservice.getNameById();



        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);
        response.put("success", true);
//        -----------------------------------
        // 로그인된 사용자 ID 가져오기
        String loggedID = "";
        boolean adminflag;
        if(session.getAttribute("employee_id") != null) {
            loggedID = (String) session.getAttribute("employee_id");
            System.out.println(loggedID + " 로그인된 아이디 디버그");
        } else if (session.getAttribute("admin") != null) {
            loggedID = (String) session.getAttribute("admin");
            System.out.println(loggedID + " 로그인된 관리자 아이디");
            adminflag = true;
            response.put("admin_flag", adminflag);
        }

        if (loggedID != null) {
            response.put("current_user_id", loggedID);
            System.out.println(loggedID + "logedd id 2차 확인");

        }


        System.out.println("확인"+ comments.size() + "/ "+ true);
// ---------------------------------------------------


        return response;
    }

    //댓글 삭제
    @DeleteMapping(value = "/board/deleteComment/{category}/{commentId}")
    public Map<String, Object> deleteComment(@PathVariable String category,
                                             @PathVariable Integer commentId, HttpSession session)throws Exception {
        boolean success = false;
        // 카테고리와 댓글 comment_num과 category로 삭제 처리

        try {
            //댓글 가져오기
            System.out.println("1");
            Map<String, Object> paramsOne = new HashMap<>();
            paramsOne.put("category", category);
            paramsOne.put("comment_num", commentId);
            commentVO commentvo = ifboardservice.getCmtByparamsOne(paramsOne);
            //if(comment)

            // 작성자 확인 후 삭제
            if(session.getAttribute("employee_id") != null) {
                System.out.println(1);
                Map<String, Object> params = new HashMap<>();
                params.put("category", category);
                params.put("comment_num", commentId);
                System.out.println("2");

                success = ifboardservice.deleteCommentByCategoryAndId(params);

            } else if (session.getAttribute("admin") != null) {
                System.out.println("3");
                Map<String, Object> params = new HashMap<>();
                params.put("category", category);
                params.put("comment_num", commentId);
                System.out.println("4");

                success = ifboardservice.deleteCommentByCategoryAndId(params);

            } else {
                System.out.println("삭제 권한 없음");
            }


        } catch (Exception e) {
            System.out.println("5");
            e.printStackTrace();
            success = false;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "댓글이 삭제되었습니다." : "댓글 삭제에 실패했습니다.");
        return response;
    }


}
