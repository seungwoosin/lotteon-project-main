package com.lotteon.service.config;


import com.lotteon.dto.requestDto.PostBannerDTO;
import com.lotteon.dto.responseDto.GetBannerDTO;
import com.lotteon.entity.config.Banner;
import com.lotteon.repository.config.BannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {
    private final BannerRepository bannerRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Caching(evict = {  @CacheEvict(value = "adminBannerCache", allEntries = true),
                        @CacheEvict(value = "bannerCache", allEntries = true)})
    public Banner insert(PostBannerDTO bannerDTO) {
        log.info(bannerDTO.toString());
        // 이미지 업로드 및 경로 설정
        if (bannerDTO.getUploadFile() != null && !bannerDTO.getUploadFile().isEmpty()) {
            String uploadedImagePath = imageService.uploadImage(bannerDTO.getUploadFile());
            if (uploadedImagePath != null) {
                bannerDTO.setBannerImg(uploadedImagePath);
            } else {
                log.error("Failed to upload banner image.");
                throw new RuntimeException("Image upload failed");
            }
        } else {
            log.warn("No image file provided for banner.");
        }
        Banner banner = modelMapper.map(bannerDTO, Banner.class);
        return bannerRepository.save(banner);
    }

    @Cacheable(value = "adminBannerCache", key = "#cateId", cacheManager = "cacheManager")
    public List<GetBannerDTO> findAllByCate(int cateId) {
        List<Banner> banners = bannerRepository.findAllByBannerLocation(cateId);

        return banners.stream()
        .map(Entity->modelMapper.map(Entity,GetBannerDTO.class))
        .collect(Collectors.toList());
    }

    @Caching(evict = {  @CacheEvict(value = "adminBannerCache", allEntries = true),
                        @CacheEvict(value = "bannerCache", allEntries = true)})
    public boolean deleteBannersById(List<Long> bannerIds) {
        try {
            for(Long bannerId : bannerIds) {
                bannerRepository.deleteById(bannerId);
            }
            return true;
        }catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
    @Caching(evict = {  @CacheEvict(value = "adminBannerCache", key = "#location"),
                        @CacheEvict(value = "bannerCache", key = "#location"),
                        @CacheEvict(value = "bannerCache", key = "'allBanner'")})
    public Banner updateBannerState(Long id, Integer state, int location) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found"));
        banner.updateBannerState(state);

        return banner;
    }

    @Cacheable(value = "bannerCache", key = "#bannerLocation", cacheManager = "cacheManager")
    public List<GetBannerDTO> selectUsingBannerAt(int bannerLocation) {
        List<Banner> banners = bannerRepository.findAllByBannerLocationAndBannerState(bannerLocation,1);
        List<GetBannerDTO> bannerList =
                banners.stream()
                        .map(Entity->modelMapper.map(Entity,GetBannerDTO.class))
                        .collect(Collectors.toList());

        log.info(bannerList.toString());

        return bannerList;
    }


    // 배너 스케줄용 메서드
    @Cacheable(value = "bannerCache", key = "'allBanner'", cacheManager = "cacheManager")
    public List<Banner> getBanners() {
        return bannerRepository.findAll(); // 모든 배너 조회
    }



}
