package com.lotteon.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBannerDTO {
    private Long id;
    private String bannerImg;
    private String bannerName;
    private String bannerSize;
    private String bannerBg;
    private String bannerLink;
    private int bannerLocation;
    private Date bannerSdate;
    private Date bannerEdate;
    private Time bannerStime;
    private Time bannerEtime;
    private Integer bannerState;
}
