package com.lotteon.service.config;

import com.lotteon.dto.responseDto.GetCopyrightDTO;
import com.lotteon.entity.config.Copyright;
import com.lotteon.repository.config.CopyrightRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CopyrightService {
    private final CopyrightRepository copyrightRepository;
    private final ModelMapper modelMapper;


    @CacheEvict(value = "footerCache", key = "'copy'")
    public Copyright updateCopyright(Long id, String copyright) {
        Copyright copy = new Copyright(id+1, copyright);
        return copyrightRepository.save(copy);
    }

    @Cacheable(value = "footerCache", key = "'copy'", cacheManager = "cacheManager")
    public GetCopyrightDTO getRecentCopyright() {
        Copyright copyright = copyrightRepository.findTopByOrderByIdDesc();
        return modelMapper.map(copyright, GetCopyrightDTO.class);
    }
}
