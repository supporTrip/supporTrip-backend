package com.supportrip.core.system.core.user.internal.application;

import com.supportrip.core.context.error.exception.badrequest.AlreadyVerifiedException;
import com.supportrip.core.context.error.exception.notfound.PhoneVerificationNotFoundException;
import com.supportrip.core.system.core.user.internal.domain.PhoneVerification;
import com.supportrip.core.system.core.user.internal.domain.PhoneVerificationRepository;
import com.supportrip.core.system.core.user.internal.domain.User;
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

    private final PhoneVerificationRepository phoneVerificationRepository;
    private final VerificationCodeGenerator verificationCodeGenerator;

    @Transactional
    public PhoneVerification createOrRenewPhoneVerification(User user, LocalDateTime now) {
        Optional<PhoneVerification> phoneVerificationOptional = phoneVerificationRepository.findByUser(user);
        if (phoneVerificationOptional.isPresent()) {
            PhoneVerification phoneVerification = phoneVerificationOptional.get();
            phoneVerification.renew(verificationCodeGenerator.generate(), now.plus(EXPIRE_DURATION));
            return phoneVerification;
        }

        return createPhoneVerification(user, now);
    }

    @Transactional
    public void verifyCode(User user, String code) {
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
