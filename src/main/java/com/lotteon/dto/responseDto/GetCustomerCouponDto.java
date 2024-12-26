package com.lotteon.dto.responseDto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class GetCustomerCouponDto {
    private Long id;
    private Long couponId;
    private String couponType;
    private String couponName;
    private String memUid;
    private int custCouponState;
    private String couponState;
    private String custCouponUdate;
    private String user;
    private String couponDiscount;
    private String couponExpiration;
    private String couponCaution;
}
