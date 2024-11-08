package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Keyword;
import me.yonghwan.myapp.dto.KeywordDto;
import me.yonghwan.myapp.repository.KeyWordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeyWordSerive {
    private final KeyWordRepository keyWordRepository;

    @Transactional
    public List<KeywordDto> saveList(List<Keyword> keyWords){
        return keyWordRepository.saveAll(keyWords).stream()
                .map(o-> new KeywordDto(o.getId(),o.getContent())).collect(Collectors.toList());

    }
    public List<KeywordDto> findByMemberId(Long memberId){

        return keyWordRepository.findByMemberId(memberId).stream()
                .map(o-> new KeywordDto(o.getId(),o.getContent())).collect(Collectors.toList());
    }
    @Transactional
    public void deleteAll(Long memberId){
        keyWordRepository.deleteByMemberId(memberId);
    }
}
