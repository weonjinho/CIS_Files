package com.cis.email.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmailFileDTO {
    private String mail_num;
    private String file_name;
    private String file_originname;
    private int file_size;
    private LocalDateTime upload_at;
    private List<String> file_name_list = new ArrayList<>();

    public String getMail_num() {
        return mail_num;
    }
    public void setMail_num(String mail_num) {
        this.mail_num = mail_num;
    }
    public String getFile_name() {
        return file_name;
    }
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
    public String getFile_originname() {
        return file_originname;
    }
    public void setFile_originname(String file_originname) {
        this.file_originname = file_originname;
    }
    public int getFile_size() {
        return file_size;
    }
    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }
    public LocalDateTime getUpload_at() {
        return upload_at;
    }
    public void setUpload_at(LocalDateTime upload_at) {
        this.upload_at = upload_at;
    }
    public List<String> getFile_name_list() {
        return file_name_list;
    }
    public void setFile_name_list(List<String> file_name_list) {
        this.file_name_list = file_name_list;
    }

    @Override
    public String toString() {
        return "EmailFileDTO{" +
                "mail_num='" + mail_num + '\'' +
                ", file_name='" + file_name + '\'' +
                ", file_originname='" + file_originname + '\'' +
                ", file_size='" + file_size + '\'' +
                ", upload_at='" + upload_at + '\'' +
                '}';
    }

}
