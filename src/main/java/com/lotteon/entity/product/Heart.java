package com.lotteon.entity.product;

import com.lotteon.dto.responseDto.GetHeartsDto;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class Heart {
    @Id
    private String id;

    @Column(name = "prod_id")
    private Long prodId;

    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "prod_price")
    private double prodPrice;

    @Column(name = "prod_discount")
    private double prodDiscount;

    @Column(name = "prod_name")
    private String prodName;

    @Column(name = "prod_img")
    private String prodImg;

    public GetHeartsDto toGetHeartsDto() {
        return GetHeartsDto.builder()
                .id(prodId)
                .img(prodImg)
                .price(prodPrice)
                .discount(prodDiscount)
                .prodName(prodName)
                .build();
    }
}
