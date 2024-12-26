package com.lotteon.repository.term;

import com.lotteon.dto.responseDto.GetTermsResponseDto;
import com.lotteon.entity.term.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface TermsRepository extends JpaRepository<Terms,Long> {

    // termsType에 따라 약관을 조회
    public List<Terms> findAllByTermsTypeContains(String termsType);


}
