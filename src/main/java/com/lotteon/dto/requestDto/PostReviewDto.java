package com.lotteon.dto.requestDto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class PostReviewDto {
    private Long orderId;
    private Long prodId;
    private Integer score;
    private String review;
    private MultipartFile reviewImg;
    private String path;
}
