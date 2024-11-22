package me.yonghwan.myapp.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileUtil {

    public File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    // 파일명에서 확장자를 제외한 이름만 추출
    public String getFileNameWithoutExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return fileName; // 확장자가 없다면 전체 파일명 반환
        }
        return fileName.substring(0, lastIndexOfDot);
    }


    // 파일명에서 확장자만 추출
    public String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없다면 빈 문자열 반환
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    public String convertToFileName(String imageUrl){
        return imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
    }





}
