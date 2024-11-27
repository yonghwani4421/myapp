package me.yonghwan.myapp.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yonghwan.myapp.aws.config.AwsS3Properties;
import me.yonghwan.myapp.helper.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 s3Client;
    private final AwsS3Properties awsS3Properties;
    private final FileUtil fileUtil;

    public String uploadFile(MultipartFile file){
        // 업로드 널값 체크 로직 추가
        if (file.isEmpty() || file == null)
            throw new NullPointerException("파일값이 null 입니다.");

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        log.info("## uploadFile fileName: {}", fileName);
        // S3에 파일 업로드
        try{
            s3Client.putObject(
                    new PutObjectRequest(
                            awsS3Properties.getBucketName()
                            , fileName
                            , fileUtil.convertMultiPartFileToFile(file)
                    )

            );
        } catch (AmazonS3Exception e){
           log.error("## S3 Exception : {}", e.getMessage());
            throw e;
        }


        // 업로드한 파일 URL 반환
        return s3Client.getUrl(awsS3Properties.getBucketName(), fileName).toString();
    }

    public String uploadFiles(List<MultipartFile> files) {
        // 다중 업로드 && 리스트 ","을 기준으로 하나의 문자열 반환
        // files 갯수 0 이면 반환 ""
        if(files == null || files.size() == 0)
            throw new NullPointerException("파일값이 null 입니다.");

        StringBuilder mergedUrl = new StringBuilder();

        for (int i = 0; i < files.size(); i++) {
            mergedUrl.append(uploadFile(files.get(i)));
            if(i < files.size() - 1) {
                mergedUrl.append(",");
            }
        }
        log.info("## uploadFiles mergedUrl: {}", mergedUrl);
        return mergedUrl.toString();
    }

    public byte[] downloadFile(String filename) {

        S3Object s3Object = s3Client.getObject(awsS3Properties.getBucketName(), filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            // e.printStackTrace();
            throw new IllegalStateException("aws s3 다운로드 error");
        }
    }

    public String deleteFile(String fileName) {
        s3Client.deleteObject(awsS3Properties.getBucketName(), fileName);
        return fileName + " removed ...";
    }

    public boolean isExistFile(String filename){
        return s3Client.doesObjectExist(awsS3Properties.getBucketName(), filename);
    }





//    private static String getFileExtension(String originalFileName) {
//        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
//    }
}
