package com.lotteon.dto.responseDto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFLotteDTO {
    private Long id;
    private String busiCompany;
    private String busiRepresentative;
    private String busiCode;
    private String busiOrderCode;
    private String busiAddr;
    private String busiAddr1;
    private String busiAddr2;

    public void splitAddress() {
        int dot = busiAddr.indexOf(',');
        this.busiAddr1 = this.busiAddr.substring(0, dot);
        this.busiAddr2 = this.busiAddr.substring(dot+1);
    }
}
