package com.lotteon.repository.product;

import com.lotteon.entity.member.Seller;
import com.lotteon.entity.product.Order;
import com.lotteon.entity.product.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    Page<OrderItem> findAllBySeller(Seller seller, Pageable pageable);

    Optional<OrderItem> findByOrder_Id(Long orderId);

    List<OrderItem> findAllByOrder_Id(Long orderId);

    List<OrderItem> findAllBySellerAndOrder_Id(Seller seller, Long orderId);

    Page<OrderItem> findAllByOrder_OrderRdateBetween(Timestamp startTimestamp, Timestamp endTimestamp, Pageable pageable);

    List<OrderItem> findAllByOrder_OrderRdateBetween(Timestamp startTimestamp, Timestamp endTimestamp);

    List<OrderItem> findAllByDeliSdateBefore(LocalDate threeDaysAgo);

    List<OrderItem> findAllByDeliSdateAfter(LocalDate threeDaysAgo);

    Page<OrderItem> findAllBySellerAndOrderDeliIdIsNotNullAndOrderDeliCompanyNotNull(Seller seller, Pageable pageable);

    Page<OrderItem> findAllBySellerAndOrderDeliIdIsNotNullAndOrderDeliCompanyNotNullOrderByDeliSdateDesc(Seller seller, Pageable pageable);

    Page<OrderItem> findAllBySellerAndOrderDeliIdAndOrderDeliCompanyNotNullOrderByDeliSdateDesc(Seller seller, String keyword, Pageable pageable);

    Page<OrderItem> findAllBySellerAndIdAndOrderDeliIdIsNotNullAndOrderDeliCompanyNotNullOrderByDeliSdateDesc(Seller seller, long l, Pageable pageable);

    Page<OrderItem> findAllBySellerAndOrder_ReceiverNameAndOrderDeliIdIsNotNullAndOrderDeliCompanyNotNullOrderByDeliSdateDesc(Seller seller, String keyword, Pageable pageable);

    List<OrderItem> findAllByOrder_IdAndState2(Long id, int i);

    List<OrderItem> findByOrder_IdAndState2(Long orderId, int i);

    Long countByState2(int i);

    Long countByOrder_OrderRdateBetween(Timestamp startTimestamp, Timestamp endTimestamp);

    Long countByState2AndOrder_OrderRdateBetween(int i, Timestamp startTimestamp, Timestamp endTimestamp);

    List<OrderItem> findAllByProduct_CategoryMappings_Category_CategoryIdAndOrder_OrderRdateBetween(Long i, Timestamp timestamp, Timestamp timestamp1);

    List<OrderItem> findAllByOrder_IdAndSeller(Long orderId, Seller seller);
}
