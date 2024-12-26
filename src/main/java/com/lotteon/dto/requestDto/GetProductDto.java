package com.lotteon.dto.requestDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class GetProductDto {
    private Long id;
    private String img;
    private String title;
    private String summary;
    private Double price;
    private Double discount;
    private Integer deli;
    private String sell_uid;
    private Integer grade;
    private Integer rating;
    private Integer reviewCnt;
    private Integer orderCnt;
}
