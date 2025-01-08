package com.cis.email.component;

import com.cis.email.dto.EmailFileDTO;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class EmailFileUtils {
    // 파일을 저장할 경로 지정
    private final String upload_path = Paths.get("C:", "UploadFiles").toString();

    // 스프링에서 제공하는 MultipartFile 인터페이스를 사용하여 파일 업로드 처리

    // 다중 파일 업로드
    public List<EmailFileDTO> uploadFiles(final List<MultipartFile> multipartFiles) {
        List<EmailFileDTO> email_files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) continue;
            email_files.add(uploadFile(multipartFile));
        }
        return email_files;
    }

    // 단일 파일 업로드
    private EmailFileDTO uploadFile(final MultipartFile multipartFile) {
        // 업로드된 파일이 없는 경우 null 리턴, 메서드 종료
        if (multipartFile.isEmpty()) return null;

        // 변수 선언
        // saveName : 디스크에 저장될 파일명
        // today : 현재 날짜
        // uploadPath : 파일의 업로드 경로
        String saveName = saveFileName(multipartFile.getOriginalFilename());
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uploadPath = getUploadPath(today) + File.separator + saveName;
        File uploadFile = new File(uploadPath);

        // 업로드한 파일이 설정한 경로인 uploadPath에 저장되도록 설정
        // transferTo 메서드를 통해 정상적으로 실행되면 파일을 생성
        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // EmailFileDTO 객체에 원본 파일명과 파일명을 저장하여 반환
        EmailFileDTO email_file = new EmailFileDTO();
        email_file.setFile_originname(multipartFile.getOriginalFilename());
        email_file.setFile_name(saveName);
        email_file.setFile_size((int)multipartFile.getSize());

        return email_file;
    }

    // 저장될 파일명 생성
    private String saveFileName(final String filename) {
        // 변수 선언
        // uuid : 32자리의 랜덤 문자열
        // 예) 2f48f241-9d64-4d16-bf56-70b9d4e0e79a → 2f48f2419d644d16bf5670b9d4e0e79a
        // extension : 업로드 한 파일의 확장자
        // 예) .png / .jpg / .txt / .xlsx / .pptx 등
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);

        // 랜덤 문자열.파일 확장자 반환
        // 예) 2f48f2419d644d16bf5670b9d4e0e79a.png
        return uuid + "." + extension;
    }

    // 업로드 경로 반환, 추가 경로 지정
    private String getUploadPath(final String addPath) {
        // 기본 업로드 경로(upload_path)에 현재 날짜를 연결하여 반환
        return makeDirectory(upload_path + File.separator + addPath);
    }

    // 업로드 폴더 생성
    private String makeDirectory(final String path) {
        // 저장하고자 하는 경로에 폴더가 없으면, 해당 경로에 폴더를 생성
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
        return dir.getPath();
    }

    // 파일 삭제 메서드
    // 1. 삭제할 파일 정보
    public void deleteFiles(final List<EmailFileDTO> files) {
        if (CollectionUtils.isEmpty(files)) return;
        for (EmailFileDTO file : files) {
            String upload_at = file.getUpload_at().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
            deleteFile(upload_at, file.getFile_name());
        }
    }
    // 2. 삭제할 파일의 추가 경로와 파일명
    private void deleteFile(final String path, final String file_name) {
        String file_path = Paths.get(upload_path, path, file_name).toString();
        deleteFile(file_path);
    }
    // 3. 삭제할 파일의 경로
    private void deleteFile(final String file_path) {
        File file = new File(file_path);
        if (file.exists()) file.delete();
    }

    // 파일 다운로드를 위한 첨부파일 조회
    // Spring에서 제공하는 Resource Interface
    // Resource(자원)에 접근하기 위해 사용 (=파일 다운로드 처리)
    public Resource downloadFileRead(final EmailFileDTO file) {
        // file : DB에서 조회한 첨부파일의 모든 정보(객체)
        // upload_at : 첨부파일이 업로드된 날짜, ofPattern 메서드로 날짜를 포맷하여 업로드된 날짜 추적
        // file_name : 디스크에 업로드된 파일명
        // file_path : 디스크에 업로드된 파일의 경로 (파일 경로 + 파일명)
        String upload_at = file.getUpload_at().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String file_name = file.getFile_name();
        Path file_path = Paths.get(upload_path, upload_at, file_name);
        // Resource Interface의 UrlResource 생성자를 이용하여 file_path에 담긴 파일의 경로를 전달
        // 만약, Resource가 없거나 Type이 파일이 아닌 경우 예외를 발생시켜 메서드 종료
        try {
            Resource resource = new UrlResource(file_path.toUri());
            if (!(resource.exists()) || !(resource.isFile())) {
                throw new RuntimeException("File Not Found : " + file_path.toString());
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("File Not Found : " + file_path.toString());
        }
    }

}
