package com.supportrip.core.system.core.user.internal.application;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomVerificationCodeGenerator implements VerificationCodeGenerator {
    private static final int MAX_CODE_LENGTH = 6;

    private final Random random = new Random();

    @Override
    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < MAX_CODE_LENGTH; i++) {
            int randomNumber = random.nextInt(10);
            stringBuilder.append(randomNumber);
        }

        return stringBuilder.toString();
    }
}
