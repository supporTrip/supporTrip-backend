package com.supportrip.core.user.service;

import com.supportrip.core.user.domain.PhoneVerification;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.AlreadyVerifiedException;
import com.supportrip.core.user.exception.PhoneVerificationNotFoundException;
import com.supportrip.core.user.repository.PhoneVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneVerificationService {
    private static final TemporalAmount EXPIRE_DURATION = Duration.ofMinutes(2);

    private final UserService userService;
    private final PhoneVerificationRepository phoneVerificationRepository;
    private final VerificationCodeGenerator verificationCodeGenerator;

    @Transactional
    public PhoneVerification createOrRenewPhoneVerification(Long userId, LocalDateTime now) {
        User user = userService.getUser(userId);

        Optional<PhoneVerification> phoneVerificationOptional = phoneVerificationRepository.findByUser(user);
        if (phoneVerificationOptional.isPresent()) {
            PhoneVerification phoneVerification = phoneVerificationOptional.get();
            phoneVerification.renew(verificationCodeGenerator.generate(), now.plus(EXPIRE_DURATION));
            return phoneVerification;
        }

        return createPhoneVerification(user, now);
    }

    @Transactional
    public void verifyCode(Long userId, String code) {
        User user = userService.getUser(userId);

        PhoneVerification phoneVerification = phoneVerificationRepository.findByUser(user)
                .orElseThrow(PhoneVerificationNotFoundException::new);

        if (phoneVerification.isVerified()) {
            throw new AlreadyVerifiedException();
        }

        phoneVerification.verify(code);
    }

    private PhoneVerification createPhoneVerification(User user, LocalDateTime now) {
        String code = verificationCodeGenerator.generate();
        LocalDateTime expiresIn = now.plus(EXPIRE_DURATION);

        PhoneVerification phoneVerification = PhoneVerification.of(user, code, expiresIn);
        phoneVerificationRepository.save(phoneVerification);
        return phoneVerification;
    }
}
