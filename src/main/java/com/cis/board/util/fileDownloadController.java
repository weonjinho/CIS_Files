package com.cis.board.util;

import com.cis.board.service.IF_board_service;
import com.cis.board.vo.fileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class fileDownloadController {

    private final IF_board_service ifboardservice;
    private final FIleDataUtil filedatautil;


    @GetMapping(value = "/fr_preview/{board_num}/{file_num}/{category}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("board_num") int boardNum,
            @PathVariable("file_num") int fileNum,
            @PathVariable("category") String category) {

        try {
            // 데이터베이스에서 파일 정보 가져오기
            List<fileVO> fileInfoList = ifboardservice.getAttach(boardNum, category);

            // 파일 정보가 없으면 404 반환
            if (fileInfoList == null || fileInfoList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // file_num과 일치하는 파일 찾기
            fileVO targetFile = null;
            for (fileVO file : fileInfoList) {
                if (file.getFile_id() == fileNum) {
                    targetFile = file;
                    break; // 첫 번째 요소를 찾으면 반복문 종료
                }
            }

            // 해당 file_num의 파일이 없으면 404 반환
            if (targetFile == null) {
                return ResponseEntity.notFound().build();
            }

            // 파일 경로 생성
            File file = new File(filedatautil.getUploadDir() + File.separator + targetFile.getSave_name());

            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.notFound().build(); // 파일이 없으면 404 반환
            }

            // Resource 객체 생성
            Resource resource = new FileSystemResource(file);

            // 파일 이름 인코딩 (한글 및 특수 문자 처리)
            String encodedFileName = java.net.URLEncoder.encode(targetFile.getOriginal_name(), "UTF-8")
                    .replace("+", "%20");

            // MIME 타입 설정
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // 기본 MIME 타입 설정
            }

            // ResponseEntity 반환
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, mimeType)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // 서버 오류 반환
        }
    }

}
