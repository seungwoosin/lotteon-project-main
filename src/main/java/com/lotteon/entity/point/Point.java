package com.lotteon.entity.point;

import com.lotteon.dto.responseDto.GetPointsDto;
import com.lotteon.entity.member.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id")
    private Customer customer;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "point_type")
    private int pointType;

    @Column(name = "point_rdate")
    @CreationTimestamp
    private LocalDate pointRdate;

    @Column(name = "point_var")
    private int pointVar;

    @Column(name = "point_etc")
    private String pointEtc;

    @Column(name = "point_expiration")
    private LocalDate pointExpiration;

    @Column(name = "point_udate")
    private LocalDateTime pointUdate;

    public GetPointsDto toGetPointsDto() {
        LocalDate today = LocalDate.now();


        String order;
        if(orderId==null){
            order="주문번호없음";
        } else {
            order=String.valueOf(orderId);
        }
        String expiration;
        if(pointType==2){
            expiration = "사용";
        } else {
            expiration = String.valueOf(pointExpiration);
        }
        String warning;
        if (ChronoUnit.DAYS.between(today, pointExpiration) < 7) {
            warning = "active";
        } else {
            warning = "none-active";
        }

        String type ;
        if(pointType==2){
            type = "사용";
        } else if(pointType==1){
            type = "적립";
        } else {
            type = "만료";
        }

        return GetPointsDto.builder()
                .id(id)
                .orderId(order)
                .rdate(pointRdate)
                .pointExpiration(expiration)
                .pointType(type)
                .pointVar(pointVar)
                .pointEtc(pointEtc)
                .warningExpiration(warning)
                .build();
    }
    public GetPointsDto toGetPointsDtoCustName() {
        LocalDate today = LocalDate.now();

        String order;
        if(orderId==null){
            order="주문번호없음";
        } else {
            order=String.valueOf(orderId);
        }
        String expiration;
        if(pointType==2){
            expiration = "사용기간만료";
        } else {
            expiration = String.valueOf(pointExpiration);
        }
        String warning;
        if (ChronoUnit.DAYS.between(today, pointExpiration) < 7) {
            warning = "active";
        } else {
            warning = "none-active";
        }

        String type ;
        if(pointType==2){
            type = "사용";
        } else if(pointType==1){
            type = "적립";
        } else {
            type = "만료";
        }

        return GetPointsDto.builder()
                .id(id)
                .orderId(order)
                .rdate(pointRdate)
                .pointExpiration(expiration)
                .pointType(type)
                .pointVar(pointVar)
                .pointEtc(pointEtc)
                .warningExpiration(warning)
                .custName(customer.getCustName())
                .custId(customer.getMember().getMemUid())
                .build();
    }

    public void changePointVar(int pointVar) {
        LocalDateTime now = LocalDateTime.now();
        if(pointVar==0){
            this.pointType = 2;
            this.pointUdate = now;
            this.pointEtc = "주문 취소";
        } else {
            this.pointVar = pointVar;
        }
    }

    public void expirationPoint() {
        this.pointType = 0;
    }

    public void updateReUsePoint() {
        this.pointType = 1;
        this.pointUdate = null;
    }

    public void updateRobPoint() {
        this.pointType = 0;
    }
}
