package com.lotteon.dto.responseDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartSessionDto {
    private long prodId;
    private int quantity;
    private List<Long> options;

}
