package com.lotteon.dto.responseDto.cartOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseOrdersDto {
    private String orderRdate;
    private Long orderId;
    private int orderState;
    private int orderQuantity;
    private int orderTotal;
    private String prodName;
    private Long prodId;
    private String prodListImg;
    private String seller;
    private int orderItemCount;
}
