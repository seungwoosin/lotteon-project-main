package com.lotteon.dto.responseDto.cartOrder;

import com.lotteon.dto.requestDto.GetProductDto;
import com.lotteon.entity.category.CategoryProductMapper;
import com.lotteon.entity.member.Seller;
import com.lotteon.entity.product.ProductOption;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDto {
    private Long Id;
    private Long sellerId;

    private String prodSummary;

    private Double prodPrice;

    private Double prodDiscount;

    private int prodDeliver;

    private String prodName;

    private String prodListImg;

    private int prodStock;

    private Integer prodOrderCnt;

    private int prodPoint;

}
