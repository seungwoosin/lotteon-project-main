package com.lotteon.dto.requestDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostAdminSellerDto {
    private String memId;
    private String memPwd;
    private String sellCompany;
    private String sellRep;
    private String sellCode;
    private String sellOrderCode;
    private String sellHp;
    private String sellFax;
    private String sellAddr;
}
