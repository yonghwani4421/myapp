package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Notice;
import me.yonghwan.myapp.dto.NoticeDto;
import me.yonghwan.myapp.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    public void delete(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
    @Transactional
    public Notice update(Long noticeId, NoticeDto noticeDto){
        Notice notice = findById(noticeId);
        notice.changeNotice(noticeDto);
        return notice;
    }
}
