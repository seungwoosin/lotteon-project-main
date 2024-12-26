package com.lotteon.repository.config;

import com.lotteon.entity.config.FCs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FCsRepository extends JpaRepository<FCs, Long> {
    FCs findTopByOrderByIdDesc();
}
