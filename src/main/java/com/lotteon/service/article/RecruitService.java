package com.lotteon.service.article;

import com.lotteon.config.MyUserDetails;
import com.lotteon.dto.requestDto.PostRecruitDto;
import com.lotteon.dto.responseDto.GetRecruitDto;
import com.lotteon.entity.article.Recruit;
import com.lotteon.entity.member.Member;
import com.lotteon.repository.article.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    public void insertRecruit(PostRecruitDto dto) {
        Member member = this.getAuthentication();
        Recruit recruit = Recruit.builder()
                .etc(dto.getEtc())
                .rdate(dto.getRdate())
                .member(member)
                .state(1)
                .title(dto.getTitle())
                .edate(LocalDate.parse(dto.getEdate()))
                .sdate(LocalDate.parse(dto.getSdate()))
                .career(dto.getCareer())
                .type(dto.getType())
                .department(dto.getDepartment())
                .build();

        recruitRepository.save(recruit);
    }

    private Member getAuthentication() {
        MyUserDetails auth = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return auth.getUser();
    }

    public Page<PostRecruitDto> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 7);
        Page<Recruit> recruits;

        recruits = recruitRepository.findAllByOrderByEdateAscIdDesc(pageable);
        Page<PostRecruitDto> dtos = recruits.map(Recruit::toGetRecruitDto);

        return dtos;
    }

    public Page<PostRecruitDto> findAllBySearch(String searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 7);
        Page<Recruit> recruits;

        if(searchType.equals("id")){
            recruits = this.findAllById(Long.parseLong(keyword),pageable);
        } else if(searchType.equals("department")){
            recruits = this.findAllByDepartment(keyword,pageable);
        } else if (searchType.equals("type")) {
            recruits = this.findAllByType(keyword,pageable);
        } else {
            recruits = this.findAllByState(keyword,pageable);

        }
        return recruits.map(Recruit::toGetRecruitDto);
    }

    private Page<Recruit> findAllByState(String keyword, Pageable pageable) {
        Page<Recruit> recruits = recruitRepository.findAllByStateOrderByEdateAscIdDesc(Integer.parseInt(keyword),pageable);
        return recruits;
    }

    private Page<Recruit> findAllByType(String keyword, Pageable pageable) {
        Page<Recruit> recruits = recruitRepository.findAllByTypeOrderByEdateAscIdDesc(keyword,pageable);
        return recruits;
    }

    private Page<Recruit> findAllByDepartment(String keyword, Pageable pageable) {
        Page<Recruit> recruits = recruitRepository.findAllByDepartmentOrderByEdateAscIdDesc(keyword,pageable);
        return recruits;
    }

    private Page<Recruit> findAllById(long id, Pageable pageable) {
        Page<Recruit> recruits = recruitRepository.findAllByIdOrderByEdateAscIdDesc(id,pageable);
        return recruits;
    }

    public GetRecruitDto findById(Long id) {
        Optional<Recruit> recruit = recruitRepository.findById(id);
        return recruit.get().toGet2RecruitDto();
    }

    public void updateRecruit(PostRecruitDto dto) {
        Optional<Recruit> recruit = recruitRepository.findById(dto.getId());
        if(recruit.isEmpty()){
            return;
        }

        recruit.get().updateRecruit(dto);
        recruitRepository.save(recruit.get());
    }
}
