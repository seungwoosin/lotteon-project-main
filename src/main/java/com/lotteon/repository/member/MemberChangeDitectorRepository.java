package com.lotteon.repository.member;

import com.lotteon.entity.member.MemberChangeDitector;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberChangeDitectorRepository extends MongoRepository<MemberChangeDitector,String> {
    MemberChangeDitector findByMemIdAndAction(Long id, String email);

    void deleteAllByMemIdAndAction(Long id, String email);
}
