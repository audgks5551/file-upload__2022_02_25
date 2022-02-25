package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    private String originalFileName;
    private String storeFileName;

    public UploadFile(String originalFilename, String storeFileName) {
        this.originalFileName = originalFilename;
        this.storeFileName = storeFileName;
    }
}
