package com.lotteon.dto.responseDto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFCsDTO {

    private Long id;
    private String hp;
    private String email;
    private String stime;
    private String etime;
    private String call;

}
