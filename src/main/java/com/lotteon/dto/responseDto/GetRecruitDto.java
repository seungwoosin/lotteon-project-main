package com.lotteon.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GetRecruitDto {
    private Long id;
    private String title;
    private String sdate;
    private String edate;
    private String etc;
    private String career;
    private String department;
    private String type;

}
