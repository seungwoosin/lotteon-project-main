package com.lotteon.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostBannerDTO {
    private String bannerName;
    private String bannerImg;
    private String bannerSize;
    private String bannerBg;
    private String bannerLink;
    private int bannerLocation;
    private Date bannerSdate;
    private Date bannerEdate;
    private Time bannerStime;
    private Time bannerEtime;

    private MultipartFile uploadFile;
}
