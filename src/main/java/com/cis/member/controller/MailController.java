package com.cis.member.controller;

import com.cis.member.dto.EmailCheckDTO;
import com.cis.member.dto.EmailRequestDTO;
import com.cis.member.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {
//    @Autowired
    private final MailSendService mailService;

    @PostMapping("/mailSend")
    public Object mailSend(@RequestBody @Valid EmailRequestDTO emailDto) throws Exception {
        System.out.println("이메일 인증 요청이 들어옴");
        System.out.println("이메일 인증 이메일 : " + emailDto.getEmail());
        return mailService.joinEmail(emailDto.getEmail());
    }

    @PostMapping("/mailauthCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckDTO emailCheckDto) {
        Boolean Checked = mailService.CheckAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum());
        if(Checked) {
            return "ok";
        }
        else{
            throw new NullPointerException("뮌가 잘못!");
        }
    }

}
