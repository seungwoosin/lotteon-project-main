package com.lotteon.dto.requestDto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setter
@Getter
public class GetDeliveryDto {
    private String deliCompany;
    private String deliveryId;
    private Long orderItemId;
    private String receiverName;
    private String prodName;
    private Integer orderItemSize;
    private Integer orderItemTotalPrice;
    private Integer prodDeli;
    private String orderItemState2;
    private String orderRdate;
}
