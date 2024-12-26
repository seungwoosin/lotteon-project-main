package com.lotteon.dto.requestDto.cartOrder;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class OrderPointAndCouponDto {
    private Long couponId;
    private Integer points;
}
