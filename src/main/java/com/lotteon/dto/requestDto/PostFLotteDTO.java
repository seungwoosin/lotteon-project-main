package com.lotteon.dto.requestDto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFLotteDTO {
    private String busiCompany;
    private String busiRepresentative;
    private String busiCode;
    private String busiOrderCode;
    private String busiAddr;
}
