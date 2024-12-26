package com.lotteon.entity.point;

import com.lotteon.dto.responseDto.GetCustomerCouponDto;
import com.lotteon.dto.responseDto.GetMyCouponDto;
import com.lotteon.dto.responseDto.cartOrder.GetCouponDto;
import com.lotteon.entity.member.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_coupon")
public class CustomerCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id")
    private Customer customer;

    @Column(name = "cust_coupon_state")
    private int couponState;

    @Column(name = "cust_coupon_udate")
    private LocalDateTime couponUDate;

    @Column(name = "coupon_expiration")
    private LocalDate couponExpiration;


    public void updateCustCouponCntMinus(){
        LocalDateTime today = LocalDateTime.now();
        this.couponUDate = today;
        this.couponState = 2;
    }

    public void updateCustCouponState(int a){
        this.couponState = a;
    }

    public void updateCustCouponStateMinus() {
        this.couponState = 1;
    }

    public GetCustomerCouponDto toGetCustomerCouponDto(){
        String uDate;
        if(couponUDate==null){
            uDate = "미사용";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            uDate = couponUDate.format(formatter);
        }
        return GetCustomerCouponDto.builder()
                .couponId(coupon.getId())
                .id(id)
                .couponName(coupon.getCouponName())
                .couponType(coupon.getCouponType())
                .custCouponState(couponState)
                .memUid(customer.getMember().getMemUid())
                .custCouponUdate(uDate)
                .build();
    }

    public GetCustomerCouponDto toGetCustomerCouponDto1(){
        String uDate;
        if(couponUDate==null){
            uDate = "미사용";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            uDate = couponUDate.format(formatter);
        }
        String realOption;
        if(coupon.getCouponDiscountOption().equals("p")) {
            realOption = "% 할인";
        } else if (coupon.getCouponDiscountOption().equals("d")){
            realOption = "배달비 할인";
        } else {
            realOption = "원 할인";
        }
        String state ;
        if(couponState==1){
            state = "미사용";
        } else if(couponState==2){
            state = "사용";
        } else{
            state = "비활성";
        }

        return GetCustomerCouponDto.builder()
                .couponId(coupon.getId())
                .id(id)
                .couponName(coupon.getCouponName())
                .couponType(coupon.getCouponType())
                .custCouponState(couponState)
                .memUid(customer.getMember().getMemUid())
                .custCouponUdate(uDate)
                .couponName(coupon.getCouponName())
                .user(coupon.getMember().getMemUid())
                .couponDiscount(coupon.getCouponDiscount()+realOption)
                .couponExpiration(coupon.getCouponExpiration())
                .couponCaution(coupon.getCouponCaution())
                .couponState(state)
                .build();
    }

    public GetMyCouponDto toGetMyCouponDto(){
        String uDate;
        if(couponUDate==null){
            uDate = "미사용";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            uDate = couponUDate.format(formatter);
        }
        String realOption;
        if(coupon.getCouponDiscountOption().equals("p")) {
            realOption = "% 할인";
        } else if (coupon.getCouponDiscountOption().equals("d")){
            realOption = "배달비 할인";
        } else {
            realOption = "원 할인";
        }
        String state ;
        if(couponState==1){
            state = "사용가능";
        } else if(couponState==2){
            state = "사용";
        } else{
            state = "비활성화";
        }

        return GetMyCouponDto.builder()
                .couponName(coupon.getCouponName())
                .couponDiscount(coupon.getCouponDiscount()+realOption)
                .couponMinPrice(coupon.getCouponMinPrice())
                .couponState(state)
                .couponExpiration(coupon.getCouponExpiration())
                .build();

    }

    public GetCouponDto toCartGetCouponDto(){
        String realOption;
        if(coupon.getCouponDiscountOption().equals("p")) {
            realOption = "% 할인";
        } else if (coupon.getCouponDiscountOption().equals("d")){
            realOption = "원 배달비 할인";
        } else {
            realOption = "원 할인";
        }
        if(coupon.getMember().getMemRole().equals("admin")){
            return GetCouponDto.builder()
                    .id(id)
                    .couponName(coupon.getCouponName())
                    .couponType(coupon.getCouponType())
                    .couponDiscountOption(realOption)
                    .couponMinPrice(coupon.getCouponMinPrice())
                    .couponDiscount(coupon.getCouponDiscount())
                    .build();
        } else {
            return GetCouponDto.builder()
                    .id(id)
                    .couponName(coupon.getCouponName())
                    .couponType(coupon.getCouponType())
                    .couponDiscountOption(realOption)
                    .couponMinPrice(coupon.getCouponMinPrice())
                    .couponDiscount(coupon.getCouponDiscount())
                    .sellId(coupon.getMember().getSeller().getId())
                    .build();
        }
    }

    public void useCoupon(int a){
        LocalDateTime now = LocalDateTime.now();
        this.couponState = a;
        this.couponUDate = now;
    }
}
