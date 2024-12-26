package com.lotteon.repository.member;

import com.lotteon.entity.member.AttendanceEvent;
import com.lotteon.entity.member.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceEventRepository extends JpaRepository<AttendanceEvent, Long> {
    Optional<AttendanceEvent> findByCustomer(Customer customer);

    List<AttendanceEvent> findByAttendanceSequenceIn(List<Integer> list);
}
