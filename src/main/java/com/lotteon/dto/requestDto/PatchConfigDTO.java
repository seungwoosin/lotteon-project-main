package com.lotteon.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchConfigDTO {
    private Long id;
    private int type;
    private String updater;
    private String title;
    private String sub;
}
