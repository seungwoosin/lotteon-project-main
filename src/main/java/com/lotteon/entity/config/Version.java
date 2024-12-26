package com.lotteon.entity.config;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`version`")
public class  Version {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mem_id")
    private Long mem_id;

    @Column(name = "ver_name")
    private String verName;

    @Column(name = "ver_content", columnDefinition = "TEXT")
    private String verContent;

    @Column(name = "ver_rdate")
    @CreationTimestamp
    private Timestamp verRdate;

}
