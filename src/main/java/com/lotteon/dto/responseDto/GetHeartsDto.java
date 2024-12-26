package com.lotteon.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetHeartsDto {
    private Long id;
    private String prodName;
    private String img;
    private Double price;
    private Double discount;
}
