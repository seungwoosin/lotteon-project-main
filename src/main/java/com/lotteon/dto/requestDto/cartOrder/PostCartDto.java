package com.lotteon.dto.requestDto.cartOrder;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostCartDto {

    private long prodId;
    private int quantity;
    private Long optionId;
    private double totalPrice;





}

