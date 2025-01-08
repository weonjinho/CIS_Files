package com.cis.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class MailSendService {
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    RedisUtil redisUtil;

    private int authNumber;

    // 추가 코드.
    public boolean CheckAuthNum(String email, String authNum){
        if(redisUtil.getData(authNum) == null){
            return false;
        }else if(redisUtil.getData(authNum).equals(email)){
            return true;
        }else{
            return false;
        }
    }

    // 임의의 6자리 양수를 반환합니다.
    public void makeRandomNumber(){
        Random r = new Random();
        String randomNumber = "";
        for(int i=0;i<6;i++){
            randomNumber += Integer.toString(r.nextInt(10));
        }
        authNumber = Integer.parseInt(randomNumber);
    }

    public String joinEmail(String email){
        makeRandomNumber();
        String setForm = "yuanzhenhao59@gmail.com";
        String toMail = email;
        String title = "사원 가입 인증 이메일 입니다.";
        String content = "저희의 프로그램을 방묵해주셔서 감사합니다." +
                            "<br/><br/>" +
                            "인증 번호는 " + authNumber + "입니다." +
                            "<br/>" +
                            "인증번호를 제대로 입력해주세요";
        mailSend(setForm, toMail, title, content);
        return Integer.toString(authNumber);
    }

    public void mailSend(String setForm, String toMail, String title, String content){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");
            helper.setFrom(setForm);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        }catch(MessagingException e){
            e.printStackTrace();
        }
        redisUtil.setDataExpire(Integer.toString(authNumber),toMail,60*5L);
    }




}
