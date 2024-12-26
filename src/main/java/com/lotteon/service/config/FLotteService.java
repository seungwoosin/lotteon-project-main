package com.lotteon.service.config;

import com.lotteon.dto.requestDto.PostFLotteDTO;
import com.lotteon.dto.responseDto.GetFLotteDTO;
import com.lotteon.entity.config.FLotte;
import com.lotteon.repository.config.FLotteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class FLotteService {
    private final ModelMapper modelMapper;
    private final FLotteRepository fLotteRepository;

    @CacheEvict(value = "footerCache", key = "'fLotte'")
    public FLotte updateFooter(PostFLotteDTO footerDTO) {
        FLotte fLotte = modelMapper.map(footerDTO, FLotte.class);
        return fLotteRepository.save(fLotte);
    }
    @Cacheable(value = "footerCache", key = "'fLotte'", cacheManager = "cacheManager")
    public GetFLotteDTO getRecentFLotte () {
        System.out.println("디비 접속 후 Footer 정보 조회");
        FLotte fLotte = fLotteRepository.findTopByOrderByIdDesc();
        GetFLotteDTO fLotteDTO= modelMapper.map(fLotte, GetFLotteDTO.class);
        fLotteDTO.splitAddress();
        return fLotteDTO;
    }


}
