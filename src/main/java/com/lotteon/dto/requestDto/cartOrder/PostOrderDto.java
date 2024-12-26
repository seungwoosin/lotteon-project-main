package com.lotteon.dto.requestDto.cartOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderDto {
    private OrderDto orderDto;
    private List<OrderItemDto> orderItemDto;
    private OrderPointAndCouponDto orderPointAndCouponDto;
}
