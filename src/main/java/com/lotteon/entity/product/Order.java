package com.lotteon.entity.product;

import com.lotteon.dto.requestDto.GetDeliveryDto;
import com.lotteon.dto.responseDto.GetDeliInfoDto;
import com.lotteon.entity.member.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id")
    private Customer customer;

    @Column(name = "order_payment")
    private int orderPayment;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_addr")
    private String receiverAddr;

    @Column(name = "receiver_hp")
    private String receiverHp;

    @Column(name = "order_total")
    private int orderTotal;

    @Setter
    @Column(name = "order_deli")
    private int orderDeli;

    @Column(name = "order_quantity")
    private int orderQuantity;

    @Setter
    @Column(name = "order_state")
    @ColumnDefault("0")
    private int orderState;

    @Column(name = "order_discount")
    private int orderDiscount;

    @Column(name = "order_rdate")
    @CreationTimestamp
    private Timestamp orderRdate;


    @Column(name = "order_req" ,columnDefinition = "TEXT")
    private String orderReq;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();

    public GetDeliveryDto toGetDeliveryDto() {
        String company;
        if(orderItems.get(0).getOrderDeliCompany()==1){
            company = "로젠택배";
        } else if(orderItems.get(0).getOrderDeliCompany()==2){
            company = "한신택배";
        } else if(orderItems.get(0).getOrderDeliCompany()==3){
            company = "우체국";
        } else {
            company = "대한통운";
        }


        Optional<Integer> maxDeli = orderItems.stream()
                .map(OrderItem::getDeli)
                .max(Integer::compareTo);

        String state ;
        if(orderState == 0){
            state = "배송준비중";
        } else if(orderState == 1) {
            state = "배송중";
        } else if(orderState == 2) {
            state = "부분완료";
        } else if(orderState == 3) {
            state = "상품확인중";
        } else {
            state = "주문완료";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date orderDate = Date.from(orderRdate.toInstant());
        String formattedDate = dateFormat.format(orderDate);

        return GetDeliveryDto.builder()
                .deliveryId(orderItems.get(0).getOrderDeliId())
                .deliCompany(company)
                .orderItemId(id)
                .orderItemSize(orderItems.size())
                .orderItemState2(state)
                .orderItemTotalPrice(orderTotal)
                .prodDeli(maxDeli.get())
                .receiverName(receiverName)
                .prodName(orderItems.get(0).getProduct().getProdName())
                .orderRdate(formattedDate)
                .build();
    }

    public GetDeliInfoDto toGetDeliInfoDto() {
        String company;
        if(orderItems.get(0).getOrderDeliCompany()==1){
            company = "로젠택배";
        } else if(orderItems.get(0).getOrderDeliCompany()==2){
            company = "한신택배";
        } else if(orderItems.get(0).getOrderDeliCompany()==3){
            company = "우체국";
        } else {
            company = "대한통운";
        }

        List<String> addrs = Arrays.asList(receiverAddr.split("/"));
        String req;
        if(orderReq == null){
            req = "요청사항 없음";
        } else {
            req = orderReq;
        }

        return GetDeliInfoDto.builder()
                .orderId(id)
                .receiverName(receiverName)
                .deliveryId(orderItems.get(0).getOrderDeliId())
                .company(company)
                .addr1(addrs.get(0))
                .addr2(addrs.get(1))
                .addr3(addrs.get(2))
                .orderReq(req)
                .build();
    }

    public void updateState(int orderState) {
        this.orderState = orderState;
    }

}
