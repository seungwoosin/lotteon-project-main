package com.lotteon.entity.config;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class VisitorCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "visitor_cnt")
    private Long visitorCnt;

    @JoinColumn(name = "visitor_date")
    private LocalDate visitorDate;
}
