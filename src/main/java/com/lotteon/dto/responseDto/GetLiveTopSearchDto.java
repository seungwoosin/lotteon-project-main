package com.lotteon.dto.responseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class GetLiveTopSearchDto {
    private String keyword;
    private Double cnt;
}
