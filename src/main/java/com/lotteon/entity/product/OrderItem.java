package com.lotteon.entity.product;

import com.lotteon.dto.requestDto.GetDeliveryDto;
import com.lotteon.dto.requestDto.GetProductNamesDto;
import com.lotteon.dto.responseDto.GetAdminOrderNameDto;
import com.lotteon.dto.responseDto.GetDeliveryDateDto;
import com.lotteon.dto.responseDto.GetReceiveConfirmDto;
import com.lotteon.entity.member.Seller;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sell_id")
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id")
    private Product product;

    @Column(name = "order_item_quantity")
    private int quantity;

    @Column(name = "order_item_total")
    private int total;

    @Column(name = "order_item_state1")
    @ColumnDefault("0")
    private int state1;

    @Column(name = "order_item_state2")
    @ColumnDefault("0")
    @Setter
    private int state2;

    @Column(name = "order_item_discount")
    private int discount;

    @Column(name = "order_item_deli")
    private int deli;

    @Column(name = "order_item_warranty")
    private Timestamp warranty;

    @Column(name = "order_deli_sdate")
    private LocalDate deliSdate;

//    @OneToMany(mappedBy = "orderItem")
//    private List<OrderItemOption> selectedOptions;

    //추가 필드
    @Setter
    @Column(name = "order_deli_id")
    private String orderDeliId;

    @Setter
    @Column(name = "order_deli_company")
    private Integer orderDeliCompany;

    @Column(name = "order_item_option_id")
    private Long optionId;

    public void updateState2(int i) {
        this.state2 = i;
    }

    public GetAdminOrderNameDto toGetAdminOrderNameDto() {
        return GetAdminOrderNameDto.builder()
                .orderItemId(id)
                .orderItemName(product.getProdName())
                .build();
    }

    public void setOrderDeliSdate(LocalDate today) {
        this.deliSdate = today;
    }

    public GetDeliveryDto toGetDeliveryDto() {
        String company;
        if(orderDeliCompany==1){
            company = "로젠택배";
        } else if(orderDeliCompany==2){
            company = "한신택배";
        } else if(orderDeliCompany==3){
            company = "우체국";
        } else {
            company = "대한통운";
        }


        Optional<Integer> maxDeli = order.getOrderItems().stream()
                .map(OrderItem::getDeli)
                .max(Integer::compareTo);

        String state ;
        if(order.getOrderState() == 0){
            state = "배송준비중";
        } else if(order.getOrderState() == 1) {
            state = "배송중";
        } else if(order.getOrderState() == 2) {
            state = "부분완료";
        } else if(order.getOrderState() == 3) {
            state = "상품확인중";
        } else {
            state = "주문완료";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date orderDate = Date.from(order.getOrderRdate().toInstant());
        String formattedDate = dateFormat.format(orderDate);

        return GetDeliveryDto.builder()
                .deliveryId(orderDeliId)
                .deliCompany(company)
                .orderItemId(id)
                .orderItemSize(order.getOrderItems().size())
                .orderItemState2(state)
                .orderItemTotalPrice(order.getOrderTotal())
                .prodDeli(maxDeli.get())
                .receiverName(order.getReceiverName())
                .prodName(product.getProdName())
                .orderRdate(formattedDate)
                .build();
    }

    public GetDeliveryDateDto toGetDeliveryDateDto(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String deliDate;
        if(deliSdate!=null){
            deliDate = dateFormat.format(deliSdate.plusDays(3));
        } else {
            deliDate = "배송준비중";
        }
        return GetDeliveryDateDto.builder()
                .prodName(product.getProdName())
                .date(deliDate)
                .build();
    }

    public GetReceiveConfirmDto toGetReceiveConfirmDto() {
        return GetReceiveConfirmDto.builder()
                .prodName(product.getProdName())
                .orderItemId(id)
                .build();
    }

    public void updateState1(int itemState1) {
        this.state1 = itemState1;
    }

    public GetProductNamesDto toGetProductNamesDto(){
        return GetProductNamesDto.builder()
                .productName(product.getProdName())
                .prodId(product.getId())
                .build();
    }

}
