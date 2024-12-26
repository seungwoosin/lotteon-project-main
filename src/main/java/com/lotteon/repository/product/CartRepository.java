package com.lotteon.repository.product;

import com.lotteon.entity.product.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByCustId(long custId);

    List<Cart> findAllByCustId(long custId);

    Optional<Cart> deleteCartById(long l);
}
