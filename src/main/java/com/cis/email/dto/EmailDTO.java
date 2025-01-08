package com.cis.email.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class EmailDTO {
    private String mail_num;            // 번호
    private String mail_title;          // 제목
    private String mail_content;        // 내용
    private String mail_check;          // 이메일 열람 여부
    private String create_at;           // 작성일
    private String recipient_id;        // 받는 사람
    private String sender_id;           // 보내는 사람
    // 파일을 저장할 리스트
    private List<MultipartFile> mail_files = new ArrayList<>();

    public String getMail_num() {
        return mail_num;
    }
    public void setMail_num(String mail_num) {
        this.mail_num = mail_num;
    }
    public String getMail_title() {
        return mail_title;
    }
    public void setMail_title(String mail_title) {
        this.mail_title = mail_title;
    }
    public String getMail_content() {
        return mail_content;
    }
    public void setMail_content(String mail_content) {
        this.mail_content = mail_content;
    }
    public String getMail_check() {
        return mail_check;
    }
    public void setMail_check(String mail_check) {
        this.mail_check = mail_check;
    }
    public String getCreate_at() {
        return create_at;
    }
    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
    public String getRecipient_id() {
        return recipient_id;
    }
    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }
    public String getSender_id() {
        return sender_id;
    }
    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
    public List<MultipartFile> getMail_files() {
        return mail_files;
    }
    public void setMail_files(List<MultipartFile> mail_files) {
        this.mail_files = mail_files;
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
                "mail_num='" + mail_num + '\'' +
                ", mail_title='" + mail_title + '\'' +
                ", mail_content='" + mail_content + '\'' +
                ", mail_check='" + mail_check + '\'' +
                ", create_at='" + create_at + '\'' +
                ", recipient_id='" + recipient_id + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", mail_files=" + mail_files +
                '}';
    }

}
