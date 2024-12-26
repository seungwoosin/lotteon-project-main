package com.lotteon.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GetSellerInfoDto {
    private String sellGrade;
    private String company;
    private String name;
    private String hp;
    private String fax;
    private String email;
    private String busiCode;
    private String address;
}
