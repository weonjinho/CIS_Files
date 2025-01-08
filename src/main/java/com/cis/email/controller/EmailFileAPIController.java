package com.cis.email.controller;

import com.cis.email.component.EmailFileUtils;
import com.cis.email.dto.EmailFileDTO;
import com.cis.email.service.IF_EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmailFileAPIController {

    private final IF_EmailService emailservice;
    private final EmailFileUtils emailFileUtils;

    // 파일 리스트 조회
    @GetMapping(value = "email_detail/{mail_num}/files/{index}")
    public EmailFileDTO emailFileListFind(@PathVariable final String mail_num, @PathVariable final String index) throws Exception {
        List<EmailFileDTO> file_list = emailservice.emailNumFind(mail_num);
        return file_list.get(Integer.parseInt(index)-1);
    }

    // 첨부파일 다운로드
    @GetMapping(value = "email_detail/{mail_num}/files/{index}/{file_name}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable final String mail_num, @PathVariable final String index, @PathVariable String file_name) throws Exception {
        // mail_num : 메일 번호
        // index : 메일 상세보기에 포함된 첨부파일 리스트의 Index
        // file_name : 첨부파일의 이름

        // file : DB에서 조회한 첨부파일의 모든 정보(객체)
        // resource : file 변수에 담긴 객체를 Resource 타입으로 조회
        EmailFileDTO file = emailservice.emailFileFind(file_name);
        Resource resource = emailFileUtils.downloadFileRead(file);
        try {
            // file_originname : 다운로드할 첨부파일의 이름
            // 실제 파일명을 저장하고, 이름이 깨지지 않도록 UTF-8 형식으로 인코딩
            String file_originname = URLEncoder.encode(file.getFile_originname(), "UTF-8");
            // ResponseEntity 클래스를 이용하여 응답에 대한 정보를 설정한다.
            // contentType : 응답에 대한 상태 코드, (파일의 Type)
            // header : 헤더 데이터, (파일명, 파일크기)
            // body : 본문 데이터 (resource)
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file_originname + "\";")
                    .header(HttpHeaders.CONTENT_LENGTH, file.getFile_size() + "")
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Filename Encoding Failed : " + file.getFile_originname());
        }
    }

}
