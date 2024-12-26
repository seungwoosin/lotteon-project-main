package com.lotteon.repository.config;

import com.lotteon.entity.config.Copyright;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopyrightRepository extends JpaRepository<Copyright, Long> {
    Copyright findTopByOrderByIdDesc();
}
