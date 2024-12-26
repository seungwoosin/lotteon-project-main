package com.lotteon.repository.product;

import com.lotteon.entity.product.OrderCancleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderCancleRepository extends MongoRepository<OrderCancleDocument,String> {
    Optional<OrderCancleDocument> findByOrderId(Long id);

    List<OrderCancleDocument> findAllByOrderId(Long id);

    Optional<OrderCancleDocument> findByCouponId(Long couponId);
}
