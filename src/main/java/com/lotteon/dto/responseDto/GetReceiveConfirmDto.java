package com.lotteon.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetReceiveConfirmDto {
    private Long orderItemId;
    private String prodName;
}
