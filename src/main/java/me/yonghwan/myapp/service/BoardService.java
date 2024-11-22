package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.aws.service.S3Service;
import me.yonghwan.myapp.domain.Board;
import me.yonghwan.myapp.domain.BoardAttachment;
import me.yonghwan.myapp.helper.FileUtil;
import me.yonghwan.myapp.repository.BoardAttachmentRepository;
import me.yonghwan.myapp.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardAttachmentRepository boardAttachmentRepository;
    private final FileUtil fileUtil;
    private final S3Service s3Service;
    public Board save(Board board){
        return boardRepository.save(board);
    }

    @Transactional
    public Board CreateBoardWithAttachmentSave(Board board, List<MultipartFile> files){
        String uploadedFileUrls = s3Service.uploadFiles(files);

        List<Long> fileSize = files.stream().map(MultipartFile::getSize).collect(Collectors.toList());

        for (int i = 0; i < uploadedFileUrls.split(",").length; i++) {
            String uploadedFileUrl = uploadedFileUrls.split(",")[i];  // 각 파일 URL
            String fileName = fileUtil.getFileNameWithoutExtension(uploadedFileUrl);
            String type = fileUtil.getFileExtension(uploadedFileUrl);
            long size = fileSize.get(i);  // fileSizes 리스트에서 해당 파일의 크기 추출

            BoardAttachment boardAttachment = new BoardAttachment(fileName, type, size, uploadedFileUrl);
            boardAttachmentRepository.save(boardAttachment);
            board.addAttachment(boardAttachment);
        }
        return boardRepository.save(board);
    }

    public Board findByIdWithAttachments(Long id){
        return boardRepository.findByIdWithAttachments(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void deleteAttachment(Long id){
        boardAttachmentRepository.deleteById(id);
    }


}
