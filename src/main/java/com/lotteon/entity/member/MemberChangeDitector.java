package com.lotteon.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MemberChangeDitector {
    @Id
    private String id;

    @Column(name = "change_date")
    @CreationTimestamp
    private LocalDate changeDate;

    @Column(name = "mem_id")
    private Long memId;

    @Column(name = "action")
    private String action;
}
