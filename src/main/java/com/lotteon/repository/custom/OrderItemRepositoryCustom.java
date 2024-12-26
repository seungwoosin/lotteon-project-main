package com.lotteon.repository.custom;

import com.lotteon.entity.member.Seller;
import com.lotteon.entity.product.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderItemRepositoryCustom {
    public List<OrderItem> selectOrderItemsByOrderId(List<Long> orderIds);
    public Page<OrderItem> selectOrderItemsBySeller(Seller seller, Pageable pageable);
}
