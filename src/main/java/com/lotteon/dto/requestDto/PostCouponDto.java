package com.lotteon.dto.requestDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostCouponDto {
    private Long memId;
    private String couponName;
    private String couponDiscount;
    private Integer couponMinPrice;
    private String couponExpiration;
    private String couponCaution;
    private Long id;
    private Integer customerCouponExpiration;
}
