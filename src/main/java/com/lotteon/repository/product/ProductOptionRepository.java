package com.lotteon.repository.product;

import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    Optional<ProductOption> findByProduct(Product product);

    List<ProductOption> findByProductId(Long productId);

    List<ProductOption> findAllByProduct(Product product);

    List<ProductOption> findAllByProductAndOptionValue(Product product, String optionValue);

    List<ProductOption> findAllByProductAndOptionState(Product product, int i);
}
