package com.cis.email.service;

import com.cis.Pagination;
import com.cis.email.dto.EmailDTO;
import com.cis.email.dto.EmailFileDTO;

import java.util.List;

public interface IF_EmailService {

    public void emailInsert(EmailDTO emailDTO) throws Exception;
    public List<EmailDTO> emailList(Object login_emp, Pagination pagination) throws Exception;
    public EmailDTO emailOne(Object login_emp, String email_num) throws Exception;
    public String emailOrderOne(EmailDTO emaildto) throws Exception;
    public int emailListCnt(Object login_emp, String filter) throws Exception;
    public void emailCheckUpdate(Object login_emp, String email_num) throws Exception;
    public void emailDelete(Object login_emp, String email_num) throws Exception;
    public void emailFileUpload(final String mail_num, final List<EmailFileDTO> files) throws Exception;
    public List<EmailFileDTO> emailNumFind(String mail_num) throws Exception;
    public EmailFileDTO emailFileFind(String file_name) throws Exception;
    public String recipientIdCheck(String recipient_id) throws Exception;
    public String recipientIdNameCheck(String recipient_id) throws Exception;

}
