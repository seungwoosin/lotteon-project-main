package com.lotteon.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetOption1Dto {
    private Long optionId;
    private String optionName;
    private String optionValue;
    private Double optionPrice;
    private Integer optionStock;
    private String type;
}
