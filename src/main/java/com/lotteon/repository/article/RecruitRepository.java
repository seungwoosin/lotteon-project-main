package com.lotteon.repository.article;

import com.lotteon.entity.article.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    Page<Recruit> findAllByOrderByEdateAscIdDesc(Pageable pageable);

    Page<Recruit> findAllByIdOrderByEdateAscIdDesc(long id, Pageable pageable);

    Page<Recruit> findAllByDepartmentOrderByEdateAscIdDesc(String keyword, Pageable pageable);

    Page<Recruit> findAllByTypeOrderByEdateAscIdDesc(String keyword, Pageable pageable);

    Page<Recruit> findAllByStateOrderByEdateAscIdDesc(int i, Pageable pageable);
}
