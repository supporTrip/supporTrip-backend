package com.supportrip.core.system.core.userlog.internal.application;

import com.supportrip.core.system.core.userlog.internal.domain.UserLog;
import com.supportrip.core.system.core.userlog.internal.domain.UserLogRepository;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.application.UserService;
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

    @Transactional
    public void appendAnonymousUserLog(String message) {
        UserLog userLog = UserLog.of(null, message);
        userLogRepository.save(userLog);
    }

    public List<UserLog> getUserLogs(Long userId) {
        User user = userService.getUser(userId);
        return userLogRepository.findByUserOrderByCreatedAt(user);
    }
}
