package com.supportrip.core.log.service;

import com.supportrip.core.log.domain.UserLog;
import com.supportrip.core.log.repository.UserLogRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLogService {
    private final UserService userService;
    private final UserLogRepository userLogRepository;

    @Transactional
    public void appendUserLog(Long userId, String message) {
        User user = userService.getUser(userId);

        UserLog userLog = UserLog.of(user, message);
        userLogRepository.save(userLog);
    }

    public List<UserLog> getUserLogs(Long userId) {
        User user = userService.getUser(userId);
        return userLogRepository.findByUserOrderByCreatedAt(user);
    }
}
