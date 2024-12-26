package com.lotteon.entity.article;

import com.lotteon.dto.requestDto.PostRecruitDto;
import com.lotteon.dto.responseDto.GetRecruitDto;
import com.lotteon.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
@Entity
public class Recruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "department")
    private String department;

    @Column(name = "career")
    private String career;

    @Column(name = "type")
    private String type;

    @Column(name = "s_date")
    private LocalDate sdate;

    @Column(name = "e_date")
    private LocalDate edate;

    @Column(name = "etc",columnDefinition = "TEXT")
    private String etc;

    @Column(name = "r_date")
    @CreationTimestamp
    private LocalDateTime rdate;

    @Column(name = "state")
    private int state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    private Member member;

    public PostRecruitDto toGetRecruitDto() {

        return PostRecruitDto.builder()
                .career(career)
                .edate(String.valueOf(edate))
                .etc(etc)
                .title(title)
                .type(type)
                .rdate(rdate)
                .sdate(String.valueOf(sdate))
                .id(id)
                .state(state)
                .department(department)
                .memUid(member.getMemUid())
                .build();
    }

    public GetRecruitDto toGet2RecruitDto (){
        return GetRecruitDto.builder()
                .title(title)
                .sdate(String.valueOf(sdate))
                .department(department)
                .type(type)
                .etc(etc)
                .career(career)
                .edate(String.valueOf(edate))
                .id(id)
                .build();
    }

    public void updateRecruit(PostRecruitDto dto) {
        this.edate = LocalDate.parse(dto.getEdate());
        this.etc = dto.getEtc();
        this.title = dto.getTitle();
        this.type = dto.getType();
        this.department = dto.getDepartment();
        this.career = dto.getCareer();
        this.sdate = LocalDate.parse(dto.getSdate());
    }
}
