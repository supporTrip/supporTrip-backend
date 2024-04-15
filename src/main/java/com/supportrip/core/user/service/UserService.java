package com.supportrip.core.user.service;

import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.dto.SignUpRequest;
import com.supportrip.core.user.exception.AlreadySignedUpUserException;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User signUp(Long userId, SignUpRequest request) {
        User user = getUser(userId);

        if (!user.isInitialUser()) {
            throw new AlreadySignedUpUserException();
        }

        user.fillInitialUserInfo(
                request.getName(),
                request.getEmail(),
                request.getGender(),
                request.getPhoneNumber(),
                request.getBirthDay()
        );

        return user;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
