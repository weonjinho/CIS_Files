package com.cis.email.controller;

import com.cis.email.component.EmailFileUtils;
import com.cis.Pagination;
import com.cis.email.dto.EmailDTO;
import com.cis.email.dto.EmailFileDTO;
import com.cis.email.service.IF_EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmailController {

    @Autowired
    private IF_EmailService emailservice;
    @Autowired
    private EmailFileUtils emailFileUtils;

    // 이메일 목록 조회, 이메일 페이지 이동
    @GetMapping(value = "email")
    public String email(@RequestParam(value = "filter", defaultValue = "all") String filter,
                        @RequestParam(defaultValue = "1") int page,
                        Model model,
                        HttpSession httpSession) throws Exception {
        Object login_emp = httpSession.getAttribute("employee_id");
        if (login_emp == null) return "total_login";

        int totalListCnt = emailservice.emailListCnt(login_emp, filter);

        Pagination pagination = new Pagination(6, totalListCnt, page);
        pagination.setSelectPage(page);
        pagination.setFilter(filter);

        List<EmailDTO> email_list = emailservice.emailList(login_emp, pagination);

        model.addAttribute("email_list", email_list);
        model.addAttribute("pagination", pagination);

        return "email/mail";
    }

    // 이메일 삭제
    @PostMapping(value = "email_delete")
    public String emailDelete(@RequestParam("num") List<String> email_num_list,
                              HttpSession httpSession) throws Exception {
        Object login_emp = httpSession.getAttribute("employee_id");
        if (login_emp == null) return "total_login";

        for (String email_num : email_num_list) {
            List<EmailFileDTO> file_name_list = emailservice.emailNumFind(email_num);
            emailFileUtils.deleteFiles(file_name_list);
            emailservice.emailDelete(login_emp, email_num);
        }

        return "redirect:email";
    }

    // 이메일 전송 페이지 이동
    @GetMapping(value = "email_send")
    public String mailSend(HttpSession httpSession) {
        if ((httpSession.getAttribute("employee_id")) == null) return "total_login";
        return "email/mail_send";
    }

    // 이메일 전송
    @PostMapping(value = "email_insert")
    public String mailInsert(@ModelAttribute EmailDTO emaildto,
                             HttpSession httpSession) throws Exception {
        Object login_emp = httpSession.getAttribute("employee_id");
        if (login_emp == null) return "total_login";

        emaildto.setSender_id((String)login_emp);
        emailservice.emailInsert(emaildto);

        String mail_num = emailservice.emailOrderOne(emaildto);
        List<EmailFileDTO> email_files = emailFileUtils.uploadFiles(emaildto.getMail_files());
        emailservice.emailFileUpload(mail_num, email_files);

        return "redirect:email";
    }

    // 전송할 이메일 작성 시, 전송하고자 하는 사람의 아이디가 있는지 검증
    @PostMapping(value = "recipient_id/check")
    @ResponseBody
    public int recipientIdCheck(@RequestParam("recipient_id") String recipient_id) throws Exception {
        if (recipient_id.equals(emailservice.recipientIdCheck(recipient_id))) return 0;
        return 1;
    }

    // 전송할 이메일 작성 시, 전송하고자 하는 사람의 아이디를 확인하여 이름을 출력하도록 설정
    @PostMapping(value = "recipient_id/name/check")
    @ResponseBody
    public String recipientIdNameCheck(@RequestParam("recipient_id") String recipient_id) throws Exception {
        return emailservice.recipientIdNameCheck(recipient_id);
    }

    // 선택한 이메일 하나의 정보 조회, 이메일 상세보기 페이지 이동
    @GetMapping(value = "email_detail")
    public String mailDetail(@RequestParam("num") String email_num,
                             Model model,
                             HttpSession httpSession) throws Exception {
        Object login_emp = httpSession.getAttribute("employee_id");
        if (login_emp == null) return "total_login";

        EmailDTO email_one = emailservice.emailOne(login_emp, email_num);
        List<EmailFileDTO> email_files = emailservice.emailNumFind(email_num);

        if (email_one.getMail_check().equals("N")) emailservice.emailCheckUpdate(login_emp, email_num);

        model.addAttribute("email_files", email_files);
        model.addAttribute("email_one", email_one);

        return "email/mail_detail";
    }

}
