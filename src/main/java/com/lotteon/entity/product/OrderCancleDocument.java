package com.lotteon.entity.product;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderCancleDocument {
    @Id
    private String id;

    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "points")
    private Integer points;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "point_udate")
    private LocalDateTime pointUdate;

    @Column(name = "point_id")
    private Long pointId;

    @Column(name = "order_item_id")
    private Long orderItemId;
}
