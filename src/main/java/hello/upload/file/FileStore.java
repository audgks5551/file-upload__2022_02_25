package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    /**
     * 복수 파일 저장
     */
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {

        // 각각의 업로드 파일 저장할 공간 생성
        List<UploadFile> storeFileResult = new ArrayList<>();

        // 하나씩 파일 저장 후 storeFileResult 리스트에 추가
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }

        return storeFileResult;
    }

    /**
     * 단일 파일 저장
     */
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        // file 비어있으면 null 반환
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 사용자가 입력한 파일 이름 추출
        String originalFilename = multipartFile.getOriginalFilename();

        // 로컬 저장소에 저장할 파일 이름 만들기
        String storeFileName = createStoreFileName(multipartFile);

        // 로컬 저장소에 파일 저장
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        // 파일 원래이름, 저장이름 반환
        return new UploadFile(originalFilename, storeFileName);
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    /**
     * 저장 파일 이름 생성
     */
    private String createStoreFileName(MultipartFile multipartFile) {
        // 파일명
        String originalFilename = multipartFile.getOriginalFilename();

        // 랜덤 UUID
        String uuid = UUID.randomUUID().toString();

        // 파일확장자
        String ext = extractExt(originalFilename);

        // 로컬 저장소에서 저장되는 파일 이름
        return uuid + "." + ext;
    }

    /**
     * 파일 확장자 추출
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


}
