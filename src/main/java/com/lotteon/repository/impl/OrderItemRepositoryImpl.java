package com.lotteon.repository.impl;

import com.lotteon.entity.member.Seller;
import com.lotteon.entity.product.OrderItem;
import com.lotteon.entity.product.QOrder;
import com.lotteon.entity.product.QOrderItem;
import com.lotteon.repository.custom.OrderItemRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Log4j2
@RequiredArgsConstructor
@Repository
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QOrderItem orderItem = QOrderItem.orderItem;
    private final QOrder order = QOrder.order;

    @Override
    public List<OrderItem> selectOrderItemsByOrderId(List<Long> orderIds) {

        List<OrderItem> orderItems= queryFactory
                                            .select(orderItem)
                                            .from(orderItem)
                                            .where(orderItem.id.in(orderIds))
                                            .fetch();

        log.info("임플에 데이터가 잘 뽑히는지 확인하자 "+orderItems);
        return orderItems;
    }

    @Override
    public Page<OrderItem> selectOrderItemsBySeller(Seller seller, Pageable pageable) {





        return null;
    }
}
