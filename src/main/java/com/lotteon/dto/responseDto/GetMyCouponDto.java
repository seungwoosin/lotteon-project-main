package com.lotteon.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class GetMyCouponDto {
    private String couponName;
    private String couponDiscount;
    private String couponState;
    private String couponExpiration;
    private String custCouponUdate;
    private Integer couponMinPrice;
}
