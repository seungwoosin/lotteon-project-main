package com.lotteon.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetDeliInfoDto {
    private Long orderId;
    private String receiverName;
    private String addr1;
    private String addr2;
    private String addr3;
    private String company;
    private String deliveryId;
    private String orderReq;
}
