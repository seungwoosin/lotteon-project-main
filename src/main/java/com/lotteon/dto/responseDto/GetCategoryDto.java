package com.lotteon.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class GetCategoryDto {
    private Long id;
    private String name;
}
