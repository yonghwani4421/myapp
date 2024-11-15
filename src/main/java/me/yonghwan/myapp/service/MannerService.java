package me.yonghwan.myapp.service;

import lombok.RequiredArgsConstructor;
import me.yonghwan.myapp.domain.Manner;
import me.yonghwan.myapp.repository.MannerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MannerService {
    private final MannerRepository mannerRepository;
    public Manner save(Manner manner){
        return mannerRepository.save(manner);
    }

    public Manner findById(Long id){
        return mannerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }


}
