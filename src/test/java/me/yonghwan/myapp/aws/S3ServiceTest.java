package me.yonghwan.myapp.aws;

import me.yonghwan.myapp.aws.config.AwsS3Properties;
import me.yonghwan.myapp.aws.service.S3Service;
import me.yonghwan.myapp.helper.FileUtil;
import me.yonghwan.myapp.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class S3ServiceTest {

    @Autowired
    S3Service s3Service;
    @Autowired
    AwsS3Properties awsS3Properties;

    @Autowired
    BoardService boardService;

    @Test
    @DisplayName("s3에 파일을 업로드 한다.")
    public void uploadFile() throws Exception{
        // given

        String localFilePath = "C:\\workspace\\test.txt";

        String bucketName = awsS3Properties.getBucketName();

        File file = new File(localFilePath);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");

        String uploadedFileUrl = s3Service.uploadFile(convertToMultipartFile(file));


        assertNotNull(uploadedFileUrl, "업로드된 파일 URL이 null이어서는 안 됩니다.");
        assertTrue(uploadedFileUrl.startsWith("https://"), "업로드된 URL이 https://로 시작해야 합니다.");
        assertTrue(uploadedFileUrl.contains(awsS3Properties.getBucketName()), "URL에 버킷 이름이 포함되어야 합니다.");
    }

    @Test
    @DisplayName("s3에 다중 파일을 업로드 한다.")
    public void uploadFiles() throws Exception{
        // given

        String localFilePath = "C:\\workspace\\test.txt";

        String bucketName = awsS3Properties.getBucketName();

        File file = new File(localFilePath);
        assertTrue(file.exists(), "파일이 존재해야 합니다.");

        List<MultipartFile> list = Arrays.asList(convertToMultipartFile(file), convertToMultipartFile(file));


        String uploadedFileUrl = s3Service.uploadFiles(list);

        String[] urls = uploadedFileUrl.split(",");

        for (String url : urls) {
            assertNotNull(url, "업로드된 파일 URL이 null이어서는 안 됩니다.");
            assertTrue(url.startsWith("https://"), "업로드된 URL이 https://로 시작해야 합니다.");
            assertTrue(url.contains(awsS3Properties.getBucketName()), "URL에 버킷 이름이 포함되어야 합니다.");
        }



    }


    public static MultipartFile convertToMultipartFile(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return new MockMultipartFile(
                    file.getName(),                // MultipartFile의 이름
                    file.getName(),                // 원본 파일 이름
                    "application/octet-stream",    // MIME 타입 (필요시 설정)
                    inputStream                    // 파일 데이터
            );
        }
    }






}