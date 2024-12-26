package com.lotteon.repository.product;

import com.lotteon.entity.product.Heart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends MongoRepository<Heart,String> {
    Optional<Heart> findByProdIdAndCustId(Long id, Long id1);

    Page<Heart> findAllByCustId(Long id, Pageable pageRequest);
}
