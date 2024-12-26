package com.lotteon.repository.config;

import com.lotteon.entity.config.VisitorCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface VisitorCountRepository extends JpaRepository<VisitorCount,Long> {
    VisitorCount findByVisitorDate(LocalDate today);
}
