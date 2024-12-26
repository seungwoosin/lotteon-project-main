package com.lotteon.repository.product;


import com.lotteon.entity.member.UserLog;
import com.lotteon.entity.product.ReviewDocu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewDocuRepository extends MongoRepository<ReviewDocu,String> {
    boolean existsByCustIdAndProdId(Long userId, Long productId);

    List<ReviewDocu> findByProdId(Long prodId);

    List<ReviewDocu> findByCustIdOrderByReviewRdateDesc(Long id);

    List<ReviewDocu> findTop3ByCustIdOrderByReviewRdateDesc(Long id);

    Page<ReviewDocu> findAllByCustIdOrderByReviewRdateDesc(Long id, Pageable pageable);

    Page<ReviewDocu> findAllByProdIdOrderByReviewRdateDesc(long prodId, Pageable pageable);

}
