package com.lotteon.dto.requestDto;

import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostProductOptionDTO {

    private long id;
    private Double additionalPrice; // 추가 가격 (옵션 선택 시 가격 증가분)
    private String optionName; // 예: "사이즈", "색상"
    private String optionValue;
    private String optionName2; // 예: "사이즈", "색상"
    private String optionValue2;
    private String optionName3; // 예: "사이즈", "색상"
    private String optionValue3; // 예: "S", "M", "L"
    private Integer stock;
    private long productId;
    private int optionState; // 필수옵션인지 아닌지

    // 추가 필드
    private List<Long> optionId;
    private List<Long> optionId2;
}
