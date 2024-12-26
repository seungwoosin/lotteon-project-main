package com.lotteon.entity.config;


import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "f_lotte")
public class FLotte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "busi_company")
    private String busiCompany;

    @Column(name = "busi_representative")
    private String busiRepresentative;

    @Column(name = "busi_code")
    private String busiCode;

    @Column(name = "busi_order_code")
    private String busiOrderCode;

    @Column(name = "busi_addr")
    private String busiAddr;
}
