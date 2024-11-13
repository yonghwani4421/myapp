package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Notice;
import me.yonghwan.myapp.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice save(Notice notice){
        return noticeRepository.save(notice);
    }
    public List<Notice> findAll(){
        return noticeRepository.findAll();
    }
    public Notice findById(Long noticeId){
        return noticeRepository.findById(noticeId).orElseThrow(()->new IllegalArgumentException("not found : " + noticeId));
    }
}
