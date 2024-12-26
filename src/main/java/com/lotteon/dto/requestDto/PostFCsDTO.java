package com.lotteon.dto.requestDto;


import jakarta.persistence.Column;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFCsDTO {
    private String hp;
    private String email;
    private String stime;
    private String etime;
    private String call;

}
