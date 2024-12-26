package com.lotteon.dto.responseDto.cartOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOrderDto {

    private String memUid;
    private String custName;
    private String custHp;
    private String custZip;
    private String custAddr1;
    private String custAddr2;
    private Integer points;
}
