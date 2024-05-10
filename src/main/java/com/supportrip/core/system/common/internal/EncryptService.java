package com.supportrip.core.system.common.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EncryptService {

    private final AesBytesEncryptor encryptor;
    private final PasswordEncoder passwordEncoder;

    public String encryptPhoneNum(String phoneNum) {
        byte[] encrypt = encryptor.encrypt(phoneNum.getBytes(StandardCharsets.UTF_8));
        return byteArrayToString(encrypt);
    }

    public String decryptPhoneNum(String encryptString) {
        byte[] decryptBytes = stringToByteArray(encryptString);
        byte[] decrypt = encryptor.decrypt(decryptBytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte abyte : bytes) {
            sb.append(abyte);
            sb.append(" ");
        }
        return sb.toString();
    }

    public byte[] stringToByteArray(String byteString) {
        String[] split = byteString.split("\\s");
        ByteBuffer buffer = ByteBuffer.allocate(split.length);
        for (String s : split) {
            buffer.put((byte) Integer.parseInt(s));
        }
        return buffer.array();
    }

    public String encryptCredentials(String credentials) {
        return passwordEncoder.encode(credentials);
    }

    public boolean matchCredentials(String encryptedCredentials, String rawCredentials) {
        return passwordEncoder.matches(rawCredentials, encryptedCredentials);
    }
}
