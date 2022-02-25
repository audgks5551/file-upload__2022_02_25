package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {


    @Value("${file.dir}") // application.properties의 "file.dir의 경로를 가져옴
    public String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("reqeust={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        /**
         * form에서 멀티 정보가 들어오므로 이것을 나눠서 컬렉션에 저장
         */
        Collection<Part> parts = request.getParts();
        log.info("part={}", parts);

        /**
         * ===== PART =====
         * name=itemName
         * header content-disposition: form-data; name="itemName"
         * submittedFileName=null
         * size=6
         * body=구두
         * ===== PART =====
         * name=file
         * header content-disposition: form-data; name="file"; filename="KakaoTalk_20220110_114049705.jpg"
         * header content-type: image/jpeg
         * submittedFileName=KakaoTalk_20220110_114049705.jpg
         * size=129289
         */
        for (Part part : parts) {


            log.info("===== PART =====");
            log.info("name={}", part.getName());
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName));
            }

            // 편의 메서드
            // content-disposition; filname
            log.info("submittedFileName={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize());
            
            // 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body={}", body);

            // 로컬 저장소에 파일을 저장하기
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath={}", fullPath);
                part.write(fullPath);
            }
        }


        return "upload-form";

    }
}
