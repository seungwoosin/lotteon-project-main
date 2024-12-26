package com.lotteon.service.config;

import com.lotteon.dto.requestDto.PostVersionDTO;
import com.lotteon.dto.responseDto.GetConfigDTO;
import com.lotteon.dto.responseDto.GetVersionDTO;
import com.lotteon.dto.responseDto.PageResponseDTO;
import com.lotteon.entity.config.Config;
import com.lotteon.entity.config.Version;
import com.lotteon.repository.config.ConfigRepository;
import com.lotteon.repository.config.VersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/*
     날짜 : 2024/10/18
     이름 : 이상훈
     내용 : 서비스 생성

     수정이력
      - 2024/10/28 김주경 - Version Config Update 캐싱
      - 2024/10/30 김주경 - config update 코드 수정
*/

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class VersionService {
    private final VersionRepository versionRepository;
    private final ConfigRepository configRepository;
    private final ConfigService configService;
    private final ModelMapper modelMapper;


    @Caching(evict = {  @CacheEvict(value = "configCache", key = "'configList'"),
                        @CacheEvict(value = "configCache", key = "'config'"),
                        @CacheEvict(value = "versionCache",allEntries = true)})

    public Version insertVersion(PostVersionDTO postVersionDTO) {
        Version version = modelMapper.map(postVersionDTO, Version.class);
        Config existingConfig = modelMapper.map(configService.getUsedConfig(), Config.class);
        Config newConfig = existingConfig.copyConfig();
        newConfig.patchSiteVersion(postVersionDTO.getVerName());
        newConfig.update(postVersionDTO.getMemUid());
        configRepository.save(existingConfig);
        configRepository.save(newConfig);
        return versionRepository.save(version);
    }

    @Cacheable(value = "versionCache",key = "'version'+#page")
    public PageResponseDTO<GetVersionDTO> getPagedVersionList(int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<GetVersionDTO> versionPage = versionRepository.findAllVersionsWithMemberUid(pageable);
        List<GetVersionDTO> versionList = versionPage.getContent();
        int total = (int) versionPage.getTotalElements();

        return PageResponseDTO.<GetVersionDTO>builder()
                .pg(page)
                .total(total)
                .dtoList(versionList)
                .size(size)
                .build();
    }

    @Caching(evict = {  @CacheEvict(value = "configCache", key = "'configList'"),
                        @CacheEvict(value = "configCache", key = "'config'"),
                        @CacheEvict(value = "versionCache",allEntries = true)})
    public boolean deleteVersionsById(List<Long> VersionIds) {
        try {
            for (Long VersionId : VersionIds) {
                versionRepository.deleteById(VersionId);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
