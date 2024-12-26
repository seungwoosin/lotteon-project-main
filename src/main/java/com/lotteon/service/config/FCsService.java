package com.lotteon.service.config;

import com.lotteon.dto.requestDto.PostFCsDTO;
import com.lotteon.dto.requestDto.PostFLotteDTO;
import com.lotteon.dto.responseDto.GetFCsDTO;
import com.lotteon.entity.config.FCs;
import com.lotteon.repository.config.FCsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FCsService {
    private final FCsRepository fCsRepository;
    private final ModelMapper modelMapper;

    @CacheEvict(value = "footerCache", key = "'fCs'")
    public FCs updateCS(PostFCsDTO csDTO) {
        FCs fCs = modelMapper.map(csDTO, FCs.class);
        return fCsRepository.save(fCs);
    }

    @Cacheable(value = "footerCache", key = "'fCs'", cacheManager = "cacheManager")
    public GetFCsDTO getRecentFCs() {
        FCs fCs = fCsRepository.findTopByOrderByIdDesc();
        return modelMapper.map(fCs, GetFCsDTO.class);
    }
}
