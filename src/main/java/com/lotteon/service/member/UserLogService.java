package com.lotteon.service.member;

import com.lotteon.entity.member.UserLog;
import com.lotteon.repository.member.UserLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLogService {

    private final UserLogRepository userLogRepository;

    public void saveUserLog(Long custId, Long prodId, String action) {
        UserLog userLog = new UserLog(custId, prodId, action, LocalDateTime.now());
        userLogRepository.save(userLog);
    }
}