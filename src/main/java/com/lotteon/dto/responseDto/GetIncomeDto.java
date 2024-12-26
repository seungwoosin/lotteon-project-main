package com.lotteon.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetIncomeDto {
    private Long id;
    private String company;
    private String busiCode;
    private Integer orderCnt;
    private Integer completeCnt;
    private Integer deliCnt;
    private Integer deliCompleteCnt;
    private Integer confirmCnt;
    private Double totalPrice;
    private Double totalRealPrice;
}
