package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Notice;
import me.yonghwan.myapp.dto.NoticeDto;
import me.yonghwan.myapp.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;


    @Transactional
    public Notice save(Notice notice){
        return noticeRepository.save(notice);
    }
}
