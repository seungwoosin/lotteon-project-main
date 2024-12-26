package com.lotteon.repository.product;

import com.lotteon.entity.product.CartItem;
import com.lotteon.entity.product.CartItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CartItemOptionRepository extends JpaRepository<CartItemOption, Long> {

    List<CartItemOption> findAllByCartItem(CartItem cartItem);
}
