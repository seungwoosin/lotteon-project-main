package com.lotteon.service.config;

import com.lotteon.entity.config.Banner;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

/*
     날짜 : 2024/10/29
     이름 : 김주경
     내용 : 배너 상태관리를 위한 서비스 파일 생성
*/

@Log4j2
@Service
@RequiredArgsConstructor
public class BannerStatusScheduler {
    private final BannerService bannerService;
    private static final int INACTIVE_STATE = 0;
    private static final int ACTIVE_STATE = 1;

    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void updateBannerStatus() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        List<Banner> banners = bannerService.getBanners(); // 모든 배너 조회 (캐시 적용)

        for (Banner banner : banners) {
            if (!isActive(banner, now.toLocalDate())) {
                deactivateIfExpired(banner, now.toLocalDate());
                continue; // 다음 배너로 넘어감
            }
            if (isCloseToChange(banner, now)) {
                updateIfNeeded(banner, now.toLocalTime());
            }
        }
    }

    private boolean isActive(Banner banner, LocalDate now) {
        LocalDate startDate = banner.getBannerSdate() != null ? banner.getBannerSdate().toLocalDate() : LocalDate.MIN;
        LocalDate endDate = banner.getBannerEdate() != null ? banner.getBannerEdate().toLocalDate() : LocalDate.MAX;
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    private void deactivateIfExpired(Banner banner, LocalDate now) {
        LocalDate endDate = banner.getBannerEdate() != null ? banner.getBannerEdate().toLocalDate() : LocalDate.MAX;
        if (now.isAfter(endDate) && banner.getBannerState() != INACTIVE_STATE) {
            bannerService.updateBannerState(banner.getId(), INACTIVE_STATE, banner.getBannerLocation());
        }
    }

    private boolean isCloseToChange(Banner banner, LocalDateTime now) {
        LocalTime startTime = banner.getBannerStime().toLocalTime();
        LocalTime endTime = banner.getBannerEtime().toLocalTime();
        LocalTime currentTime = now.toLocalTime();

        log.info("close to update banner : "+banner);
        boolean isActivationClose = !currentTime.isBefore(startTime.minusMinutes(5)) && currentTime.isBefore(startTime.plusMinutes(5));
        boolean isDeactivationClose = !currentTime.isBefore(endTime.minusMinutes(5)) && currentTime.isBefore(endTime.plusMinutes(5));

        return isActivationClose || isDeactivationClose;
    }

    private void updateIfNeeded(Banner banner, LocalTime now) {
        log.info("need to update banner : "+banner);
        log.info("now times : "+now);
        LocalTime startDateTime = banner.getBannerStime().toLocalTime();
        LocalTime endDateTime = banner.getBannerEtime().toLocalTime();

        boolean shouldActivate = now.isAfter(startDateTime) && now.isBefore(endDateTime);
        int newState = shouldActivate ? ACTIVE_STATE : INACTIVE_STATE;

        if (banner.getBannerState() != newState) {
            bannerService.updateBannerState(banner.getId(), newState, banner.getBannerLocation());
        }
    }
}
