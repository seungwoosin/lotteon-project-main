package com.lotteon.dto.requestDto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchLogoDTO {
    private Long id;
    private String updater;
    private MultipartFile file1;
    private MultipartFile file2;
    private MultipartFile file3;
}
