package com.supportrip.core.system.common.external;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SmsService {
    private final DefaultMessageService defaultMessageService;
    private final String senderPhoneNumber;

    public SmsService(@Value("${sms.api-key}") String apiKey,
                      @Value("${sms.api-secret-key}") String apiSecretKey,
                      @Value("${sms.domain}") String domain,
                      @Value("${sms.sender-phone-number}") String senderPhoneNumber) {
        validatePhoneNumber(senderPhoneNumber);
        this.senderPhoneNumber = senderPhoneNumber;
        this.defaultMessageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, domain);
    }

    public void sendOne(String text, String receiverPhoneNumber) {
        Message message = createMessage(text, receiverPhoneNumber);
        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        defaultMessageService.sendOne(request);
    }

    public void send(String text, List<String> receiverPhoneNumbers) {
        List<Message> messages = receiverPhoneNumbers.stream()
                .map(phoneNumber -> createMessage(text, phoneNumber))
                .toList();

        try {
            MultipleDetailMessageSentResponse response = defaultMessageService.send(messages, false);
        } catch (NurigoMessageNotReceivedException exception) {
            log.warn("Failed to send SMS : ", exception);
        } catch (Exception exception) {
            log.error("An unknown error occurred during SMS sending. ", exception);
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        final String regex = "^010\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

//        if (!matcher.matches()) {
//            throw new IllegalArgumentException("Invalid sender number format.");
//        }
    }

    @NotNull
    private Message createMessage(String text, String phoneNumber) {
        Message message = new Message();
        message.setFrom(senderPhoneNumber);
        message.setTo(phoneNumber);
        message.setText(text);

        return message;
    }
}
