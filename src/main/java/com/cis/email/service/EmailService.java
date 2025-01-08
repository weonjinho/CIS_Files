package com.cis.email.service;

import com.cis.Pagination;
import com.cis.email.dto.EmailDTO;
import com.cis.email.dto.EmailFileDTO;
import com.cis.email.repository.IF_EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class EmailService implements IF_EmailService {

    @Autowired
    private IF_EmailRepository emailrepository;

    @Override
    public void emailInsert(EmailDTO emailDTO) throws Exception {
        emailrepository.emailInsert(emailDTO);
    }

    @Override
    public List<EmailDTO> emailList(Object login_emp, Pagination pagination) throws Exception {
        return switch (pagination.getFilter()) {
            case "unread" -> emailrepository.emailSelectAll(login_emp, pagination, "N");
            case "read" -> emailrepository.emailSelectAll(login_emp, pagination, "Y");
            default -> emailrepository.emailSelectAll(login_emp, pagination, null);
        };
    }

    @Override
    public EmailDTO emailOne(Object login_emp, String email_num) throws Exception {
        return emailrepository.emailSelectOne(login_emp, email_num);
    }

    @Override
    public String emailOrderOne(EmailDTO emaildto) throws Exception {
        return emailrepository.emailSelectOrderOne(emaildto);
    }

    @Override
    public int emailListCnt(Object login_emp, String filter) throws Exception {
        return emailrepository.emailSelectAllCnt(login_emp, filter);
    }

    @Override
    public void emailCheckUpdate(Object login_emp, String email_num) throws Exception {
        emailrepository.emailUpdate(login_emp, email_num);
    }

    @Override
    public void emailDelete(Object login_emp, String email_num) throws Exception {
        emailrepository.emailDelete(login_emp, email_num);
    }

    @Override
    public void emailFileUpload(final String mail_num, final List<EmailFileDTO> email_files) throws Exception {
        if (CollectionUtils.isEmpty(email_files)) return;

        for (EmailFileDTO email_file : email_files) {
            email_file.setMail_num(mail_num);
        }

        emailrepository.emailFileInsert(email_files);
    }

    @Override
    public List<EmailFileDTO> emailNumFind(String mail_num) throws Exception {
        return emailrepository.emailNumFind(mail_num);
    }

    @Override
    public EmailFileDTO emailFileFind(String file_name) throws Exception {
        return emailrepository.emailFileFind(file_name);
    }

    @Override
    public String recipientIdCheck(String recipient_id) throws Exception {
        return emailrepository.findEmployeeId(recipient_id);
    }

    @Override
    public String recipientIdNameCheck(String recipient_id) throws Exception {
        return emailrepository.findEmployeeName(recipient_id);
    }

}
