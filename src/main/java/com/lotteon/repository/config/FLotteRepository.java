package com.lotteon.repository.config;

import com.lotteon.entity.config.FLotte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FLotteRepository extends JpaRepository<FLotte, Long> {
    FLotte findTopByOrderByIdDesc();
}
