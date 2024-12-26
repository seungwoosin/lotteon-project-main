package com.lotteon.dto.responseDto.cartOrder;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GetCouponDto {
    private Long id;
    private String couponName;
    private String couponDiscountOption;
    private Integer couponDiscount;
    private Integer couponMinPrice;
    private String couponType;
    private Long sellId;
}
