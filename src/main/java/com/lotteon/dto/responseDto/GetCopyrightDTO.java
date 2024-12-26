package com.lotteon.dto.responseDto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCopyrightDTO {

    private Long id;
    private String copy;
}
